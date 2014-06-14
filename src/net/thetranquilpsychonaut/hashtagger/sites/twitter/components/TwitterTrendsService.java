package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.events.TwitterTrendsEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.Twitter;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.TrendLocation;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Trends;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.TrendsPrefs;

import java.util.List;

/**
 * Created by itwenty on 6/5/14.
 */
public class TwitterTrendsService extends Service
{
    public static final long TRENDS_UPDATE_INTERVAL = 1000 * 60 * 60; // one hpur

    // choice of selected location
    public static final int LOCAL  = 0;
    public static final int GLOBAL = 1;

    // statuses for TwitterTrendsEvent
    public static final int TWITTER_NOT_LOGGED_IN = 100;
    public static final int TRENDS_NOT_AVAILABLE  = 101;
    public static final int TRENDS_FOUND          = 102;

    private HandlerThread         trendsFetcherThread;
    private Handler               trendsFetcherHandler;
    private TrendsFetcherRunnable trendsFetcherRunnable;
    private IBinder               twitterTrendsBinder;
    private TwitterTrendsEvent    event;
    private int                   trendsChoice;

    @Override
    public void onCreate()
    {
        super.onCreate();
        trendsFetcherThread = new HandlerThread( "TrendsFetcherThread" );
        trendsFetcherThread.start();
        trendsFetcherHandler = new Handler( trendsFetcherThread.getLooper() );
        twitterTrendsBinder = new TwitterTrendsBinder();
        trendsFetcherRunnable = new TrendsFetcherRunnable();
        Helper.debug( "TwitterTrendsService created" );
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if ( null != trendsFetcherThread )
        {
            trendsFetcherThread.quit();
        }
        Helper.debug( "TwitterTrendsService destroyed" );
    }

    private void fetchNewLocalTrends()
    {
        long localTrendsLastUpdated = TrendsPrefs.getLocalTrendsLastUpdated();
        if ( localTrendsLastUpdated != -1 && localTrendsLastUpdated + TRENDS_UPDATE_INTERVAL > System.currentTimeMillis() )
        {
            Helper.debug( "Stored local trends are fresher than our update interval" );
            event = new TwitterTrendsEvent( TrendsPrefs.getLocalTrends(), LOCAL, TRENDS_FOUND );
            notifyTrendsFound( event, true );
        }
        else
        {
            if ( AccountPrefs.areTwitterDetailsPresent() )
            {
                Helper.debug( "User is logged in to Twitter" );
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
                event = new TwitterTrendsEvent( null, LOCAL, TWITTER_NOT_LOGGED_IN );
                notifyTrendsFound( event, true );
            }
        }
    }

    private void fetchTrendsForLastKnownLocation( Location lastLoc )
    {
        Helper.debug( "fetchTrendsForLastKnownLocation called" );
        TrendLocation trendsLoc = getClosestTrendLocation( lastLoc.getLatitude(), lastLoc.getLongitude() );
        if ( null != trendsLoc )
        {
            Helper.debug( String.format( "Location closest to user's location for which trends are available is %s", trendsLoc.getName() ) );
            Trends trends = getTrendsForLocation( trendsLoc.getWoeid() );
            if ( null != trends )
            {
                Helper.debug( String.format( "Trends found for location %s", trends.getLocations().get( 0 ).getName() ) );
                event = new TwitterTrendsEvent( Helper.createTrendsArrayList( trends ), LOCAL, TRENDS_FOUND );
                notifyTrendsFound( event, false );
            }
            else
            {
                Helper.debug( String.format( "Trends not found for location %s", trendsLoc.getName() ) );
                fetchTrendsForCountry();
            }
        }
        else
        {
            Helper.debug( String.format( "No location with trends info found close to %f %f", lastLoc.getLatitude(), lastLoc.getLongitude() ) );
            fetchTrendsForCountry();
        }
    }

    private void fetchTrendsForCountry()
    {
        Helper.debug( "fetchTrendsForCountry called" );
        String countryIso = fetchSimCountryIso();
        if ( !TextUtils.isEmpty( countryIso ) )
        {
            Helper.debug( "Country ISO from SIM is " + countryIso );
            TrendLocation countryLoc = getTrendLocationForCountry( countryIso );
            if ( null != countryLoc )
            {
                Helper.debug( String.format( "Trends location for country %s is %s", countryIso, countryLoc.getCountry() ) );
                Trends trends = getTrendsForLocation( countryLoc.getWoeid() );
                if ( null != trends )
                {
                    Helper.debug( String.format( "Trends found for location %s", trends.getLocations().get( 0 ).getName() ) );
                    event = new TwitterTrendsEvent( Helper.createTrendsArrayList( trends ), LOCAL, TRENDS_FOUND );
                    notifyTrendsFound( event, false );
                }
                else
                {
                    Helper.debug( String.format( "No trends for country %s", countryLoc.getCountry() ) );
                    event = new TwitterTrendsEvent( null, LOCAL, TRENDS_NOT_AVAILABLE );
                    notifyTrendsFound( event, true );
                }
            }
            else
            {
                Helper.debug( String.format( "No trends for country with ISO %s", countryIso ) );
                event = new TwitterTrendsEvent( null, LOCAL, TRENDS_NOT_AVAILABLE );
                notifyTrendsFound( event, true );
            }
        }
        else
        {
            Helper.debug( "Could not find country ISO from SIM" );
            event = new TwitterTrendsEvent( null, LOCAL, TRENDS_NOT_AVAILABLE );
            notifyTrendsFound( event, true );
        }
    }

    public void fetchTrends( int trendsChoice )
    {
        Helper.debug( "fetchTrends called" );
        this.trendsChoice = trendsChoice;
        trendsFetcherHandler.removeCallbacks( trendsFetcherRunnable );
        trendsFetcherHandler.post( trendsFetcherRunnable );
    }

    private void fetchGlobalTrends()
    {
        Helper.debug( "fetchGlobalTrends called" );
        TwitterTrendsEvent tte;
        long globalTrendsLastUpdated = TrendsPrefs.getGlobalTrendsLastUpdated();
        if ( globalTrendsLastUpdated != -1 && globalTrendsLastUpdated + TRENDS_UPDATE_INTERVAL > System.currentTimeMillis() )
        {
            Helper.debug( "Stored global trends are fresher than our update interval" );
            event = new TwitterTrendsEvent( TrendsPrefs.getGlobalTrends(), GLOBAL, TRENDS_FOUND );
            notifyTrendsFound( event, true );
        }
        else
        {
            if ( AccountPrefs.areTwitterDetailsPresent() )
            {
                Helper.debug( "User is logged in to twitter" );
                Trends globalTrends = getTrendsForLocation( 1 );
                if ( null != globalTrends )
                {
                    Helper.debug( "Global trends found" );
                    tte = new TwitterTrendsEvent( Helper.createTrendsArrayList( globalTrends ), GLOBAL, TRENDS_FOUND );
                    notifyTrendsFound( tte, false );
                }
                else
                {
                    Helper.debug( "Global trends not found" );
                    tte = new TwitterTrendsEvent( null, GLOBAL, TRENDS_NOT_AVAILABLE );
                    notifyTrendsFound( tte, true );
                }
            }
            else
            {
                Helper.debug( "User not logged in to Twitter" );
                event = new TwitterTrendsEvent( null, GLOBAL, TWITTER_NOT_LOGGED_IN );
                notifyTrendsFound( event, true );
            }
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

    private void notifyTrendsFound( final TwitterTrendsEvent event, boolean cached )
    {
        if ( event.getStatus() == TRENDS_FOUND && !cached )
        {
            Helper.debug( "Trends found from net. Saving locally" );
            switch ( event.getTrendingChoice() )
            {
                case 1:
                    TrendsPrefs.setGlobalTrends( event.getTrends() );
                    break;
                case 0: // fall through
                default:
                    TrendsPrefs.setLocalTrends( event.getTrends() );
                    break;
            }
        }
        new Handler( Looper.getMainLooper() ).post( new Runnable()
        {
            @Override
            public void run()
            {
                // Subscriber : TrendingHashtagsFragment : onTwitterTrendsFound()
                HashtaggerApp.bus.post( event );
            }
        } );
    }

    private Trends getTrendsForLocation( int woeid )
    {
        Trends trends = null;
        try
        {
            trends = Twitter.api().getTrendsForPlace( woeid ).get( 0 );
        }
        catch ( Exception e )
        {
            Helper.debug( String.format( "Error while finding trends for woeid %d : %s", woeid, e.getMessage() ) );
        }
        return trends;
    }

    private TrendLocation getClosestTrendLocation( double latitude, double longitude )
    {
        TrendLocation location = null;
        try
        {
            List<TrendLocation> locs = Twitter.api().getClosestTrendLocations( latitude, longitude );
            if ( null == locs || locs.size() == 0 )
            {
                throw new Exception( "No closest locations found" );
            }
            location = locs.get( 0 );
        }
        catch ( Exception e )
        {
            Helper.debug( String.format( "Error while finding closest trends for %f %f : %s", latitude, longitude, e.getMessage() ) );
        }
        return location;
    }

    private TrendLocation getTrendLocationForCountry( String countryIso )
    {
        TrendLocation location = null;
        try
        {
            List<TrendLocation> locs = Twitter.api().getAvailableTrends();
            if ( null == locs || locs.size() == 0 )
            {
                throw new Exception( "No available trends found" );
            }
            for ( TrendLocation l : locs )
            {
                if ( null != l.getCountryCode() && l.getCountryCode().equalsIgnoreCase( countryIso ) )
                {
                    Helper.debug( "Trends found for country code " + countryIso + " : " + l.toString() );
                    location = l;
                    break;
                }
            }
            if ( null == location )
            {
                throw new Exception( "Trends not available for country " + countryIso );
            }
        }
        catch ( Exception e )
        {
            Helper.debug( String.format( "Error while searching available trends : %s", e.getMessage() ) );
        }
        return location;
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
            switch ( trendsChoice )
            {
                case 1:
                    fetchGlobalTrends();
                    break;
                case 0: // fall through
                default:
                    fetchNewLocalTrends();
                    break;
            }
        }
    }
}
