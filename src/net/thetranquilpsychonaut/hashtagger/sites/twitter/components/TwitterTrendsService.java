package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.*;
import android.telephony.TelephonyManager;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.TwitterConfig;
import net.thetranquilpsychonaut.hashtagger.events.TwitterTrendsEvent;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.TrendsPrefs;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.List;

/**
 * Created by itwenty on 6/5/14.
 */
public class TwitterTrendsService extends Service
{
    public static final long TRENDS_UPDATE_INTERVAL = 1000 * 60 * 60; // one hpur
    public static final long KEEP_OLD_TRENDS_DURATION = 1000 * 60 * 60 * 24 * 3; // 3 days

    // statuses for TwitterTrendsEvent
    public static final int TWITTER_NOT_LOGGED_IN = 100;
    public static final int TRENDS_NOT_AVAILABLE  = 101;
    public static final int TRENDS_FOUND          = 102;

    private HandlerThread         trendsFetcherThread;
    private Handler               trendsFetcherHandler;
    private TrendsFetcherRunnable trendsFetcherRunnable;
    private IBinder               twitterTrendsBinder;
    private TwitterTrendsEvent    event;
    private Twitter               twitter;

    @Override
    public void onCreate()
    {
        super.onCreate();
        trendsFetcherThread = new HandlerThread( "TrendsFetcherThread" );
        trendsFetcherThread.start();
        trendsFetcherHandler = new Handler( trendsFetcherThread.getLooper() );
        twitterTrendsBinder = new TwitterTrendsBinder();
        trendsFetcherRunnable = new TrendsFetcherRunnable();
        twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
        Helper.debug( "TwitterTrendsService created" );
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if ( null != trendsFetcherThread )
            trendsFetcherThread.quit();
        Helper.debug( "TwitterTrendsService destroyed" );
    }

    private void updateAccessToken()
    {
        twitter.setOAuthAccessToken( null );
        twitter.setOAuthAccessToken( new AccessToken( AccountPrefs.getTwitterAccessToken(), AccountPrefs.getTwitterAccessTokenSecret() ) );
    }

    private void fetchNewTrends()
    {
        Helper.debug( "fetchNewTrends called" );
        long trendsLastUpdated = TrendsPrefs.getTrendsLastUpdated();
        if ( trendsLastUpdated != -1 && trendsLastUpdated + TRENDS_UPDATE_INTERVAL > System.currentTimeMillis() )
        {
            Helper.debug( "Stored trends are fresher than our update interval" );
            event = new TwitterTrendsEvent( TrendsPrefs.getTrends(), TrendsPrefs.getPlace(), TRENDS_FOUND );
            notifyTrendsFound( event, true );
        }
        else
        {
            if ( AccountPrefs.areTwitterDetailsPresent() )
            {
                Helper.debug( "User is logged in to Twitter" );
                updateAccessToken();
                Location lastLoc = getLastKnownLocation();
                if ( null != lastLoc )
                {
                    Helper.debug( String.format( "User's last known location is %f %f", lastLoc.getLatitude(), lastLoc.getLongitude() ) );
                    fetchTrendsForLastKnownLocation( lastLoc );
                }
                else
                {
                    Helper.debug( "Last known location is null" );
                    fetchTrendsForCountry();
                }
            }
            else
            {
                Helper.debug( "User not logged in to Twitter" );
                if ( trendsLastUpdated != -1 && trendsLastUpdated + KEEP_OLD_TRENDS_DURATION > System.currentTimeMillis() )
                {
                    Helper.debug( "Old trends newer than three days found in SharedPreferences" );
                    event = new TwitterTrendsEvent( TrendsPrefs.getTrends(), TrendsPrefs.getPlace(), TRENDS_FOUND );
                    notifyTrendsFound( event, true );
                }
                else
                {
                    Helper.debug( "Old trends not found or were older than three days" );
                    event = new TwitterTrendsEvent( null, null, TWITTER_NOT_LOGGED_IN );
                    notifyTrendsFound( event, true );
                }
            }
        }
    }

    private void fetchTrendsForLastKnownLocation( Location lastLoc )
    {
        Helper.debug( "fetchTrendsForLastKnownLocation called" );
        twitter4j.Location trendsLoc = getClosestTrendsLocation( new GeoLocation( lastLoc.getLatitude(), lastLoc.getLongitude() ) );
        if ( null != trendsLoc )
        {
            Helper.debug( String.format( "Location closest to user's location for which trends are available is %s", trendsLoc.getName() ) );
            Trends trends = getTrendsForLocation( trendsLoc.getWoeid() );
            if ( null != trends )
            {
                Helper.debug( String.format(  "Trends found for location %s", trends.getLocation().getName() ) );
                event = new TwitterTrendsEvent( Helper.createTrendsArrayList( trends ), trends.getLocation().getName(), TRENDS_FOUND );
                notifyTrendsFound( event, false );
            }
            else
            {
                Helper.debug( String.format(  "Trends not found for location %s", trendsLoc.getName() ) );
                fetchTrendsForCountry();
            }
        }
        else
        {
            Helper.debug( String.format(  "No location with trends info found close to %d %d", lastLoc.getLatitude(), lastLoc.getLongitude() ) );
            fetchTrendsForCountry();
        }
    }

    private void fetchTrendsForCountry()
    {
        Helper.debug( "fetchTrendsForCountry called" );
        String countryIso = fetchSimCountryIso();
        if ( null != countryIso )
        {
            Helper.debug( "Country ISO from SIM is " + countryIso );
            twitter4j.Location countryLoc = getTrendsLocationForCountry( countryIso );
            if ( null != countryLoc )
            {
                Helper.debug( String.format( "Trends location for country %s is %s", countryIso, countryLoc.getCountryName() ) );
                Trends trends = getTrendsForLocation( countryLoc.getWoeid() );
                if ( null != trends )
                {
                    Helper.debug( String.format(  "Trends found for location %s", trends.getLocation().getName() ) );
                    event = new TwitterTrendsEvent( Helper.createTrendsArrayList( trends ), trends.getLocation().getCountryName(), TRENDS_FOUND );
                    notifyTrendsFound( event, false );
                }
                else
                {
                    Helper.debug( String.format( "No trends for country %s", countryLoc.getCountryName() ) );
                    fetchGlobalTrends();
                }
            }
            else
            {
                Helper.debug( String.format( "No trends for country with ISO %s", countryIso ) );
                fetchGlobalTrends();
            }
        }
        else
        {
            Helper.debug( "Could not find country ISO from SIM" );
            fetchGlobalTrends();
        }
    }

    public void fetchTrends()
    {
        trendsFetcherHandler.post( trendsFetcherRunnable );
    }

    private void fetchGlobalTrends()
    {
        Helper.debug( "fetchGlobalTrends called" );
        TwitterTrendsEvent tte;
        Trends globalTrends = getTrendsForLocation( 1 );
        if ( null == globalTrends )
        {
            Helper.debug( "Global trends unavailable. We are out of luck :/" );
            tte = new TwitterTrendsEvent( null, null, TRENDS_NOT_AVAILABLE );
            notifyTrendsFound( tte, true );
        }
        else
        {
            Helper.debug( "Global trends found" );
            tte = new TwitterTrendsEvent( Helper.createTrendsArrayList( globalTrends ), "World", TRENDS_FOUND );
            notifyTrendsFound( tte, true );
        }
    }

    private String fetchSimCountryIso()
    {
        String countryIso;
        TelephonyManager manager = ( TelephonyManager ) HashtaggerApp.app.getSystemService( Context.TELEPHONY_SERVICE );
        countryIso = manager.getSimCountryIso();
        Helper.debug( null == countryIso ? "null" : countryIso );
        return countryIso;
    }

    private void notifyTrendsFound( final TwitterTrendsEvent event, boolean local )
    {
        if ( event.getStatus() == TRENDS_FOUND && !local )
        {
            Helper.debug( "Trends found from net. Saving to SharedPreferences" );
            TrendsPrefs.addTrends( event.getTrends(), event.getPlace() );
        }
        new Handler( Looper.getMainLooper() ).post( new Runnable()
        {
            @Override
            public void run()
            {
                HashtaggerApp.bus.post( event );
            }
        } );
    }

    private Trends getTrendsForLocation( int woeid )
    {
        Trends trends = null;
        try
        {
            trends = twitter.getPlaceTrends( woeid );
        }
        catch ( TwitterException e )
        {
            Helper.debug( String.format( "Error while finding trends for woeid %d : %s", woeid, e.getMessage() ) );
        }
        return trends;
    }

    private twitter4j.Location getClosestTrendsLocation( GeoLocation geoLocation )
    {
        twitter4j.Location loc = null;
        try
        {
            List<twitter4j.Location> locs = twitter.getClosestTrends( geoLocation );
            if ( null == locs || locs.size() == 0 )
            {
                throw new TwitterException( "No closest locations found" );
            }
            loc = locs.get( 0 );
        }
        catch ( TwitterException e )
        {
            Helper.debug( String.format( "Error while finding closest trends for %f %f : %s", geoLocation.getLatitude(), geoLocation.getLongitude(), e.getMessage() ) );
        }
        return loc;
    }

    private twitter4j.Location getTrendsLocationForCountry( String countryIso )
    {
        twitter4j.Location loc = null;
        try
        {
            List<twitter4j.Location> locs = twitter.getAvailableTrends();
            if ( null == locs || locs.size() == 0 )
            {
                throw new TwitterException( "No available trends found" );
            }
            for ( twitter4j.Location l : locs )
            {
                if ( l.getCountryCode().equalsIgnoreCase( countryIso ) )
                {
                    loc = l;
                    break;
                }
            }
            if ( null == loc )
            {
                throw new TwitterException( "Trends not available for country " + countryIso );
            }
        }
        catch ( TwitterException e )
        {
            Helper.debug( String.format( "Error while searching available trends : %s", e.getMessage() ) );
        }
        return loc;
    }

    private Location getLastKnownLocation()
    {
        LocationManager locationManager = ( LocationManager ) HashtaggerApp.app.getSystemService( Context.LOCATION_SERVICE );
        return locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
    }

    @Override
    public IBinder onBind( Intent intent )
    {
        return twitterTrendsBinder;
    }

    public class TwitterTrendsBinder extends Binder
    {
        public TwitterTrendsService getService()
        {
            return TwitterTrendsService.this;
        }
    }

    private class TrendsFetcherRunnable implements Runnable
    {
        @Override
        public void run()
        {
            Helper.debug( "trendsFetcherRunnable running!" );
            fetchNewTrends();
            trendsFetcherHandler.postDelayed( this, TRENDS_UPDATE_INTERVAL );
        }
    }
}
