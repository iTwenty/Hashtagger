package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.telephony.TelephonyManager;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.TwitterConfig;
import net.thetranquilpsychonaut.hashtagger.events.TwitterTrendsEvent;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.List;

/**
 * Created by itwenty on 6/2/14.
 */
public class TwitterTrendsFetcher implements LocationListener
{
    private static final long LOCATION_UPDATE_INTERVAL = 1000 * 60 * 60 * 24; // one day

    // codes for msg.what
    private static final int NETWORK_LOCATION_FOUND = 0;
    private static final int SIM_COUNTRY_FOUND      = 1;
    private static final int NO_LOCATION_FOUND      = 2;

    // codes for TwitterTrendsEvent
    public static final int TWITTER_NOT_LOGGED_IN = 100;
    public static final int TRENDS_NOT_AVAILABLE  = 101;
    public static final int TRENDS_FOUND          = 102;

    private HandlerThread      mThread;
    private Handler            mHandler;
    private TwitterTrendsEvent event;
    private Twitter            twitter;

    public TwitterTrendsFetcher()
    {
        mThread = new HandlerThread( "TwitterTrendsFetcher" );
        mThread.start();
        mHandler = new Handler( mThread.getLooper() );
        twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
        mHandler.post( new Runnable()
        {
            @Override
            public void run()
            {
                fetchTrends();
            }
        } );
    }

    public void fetchTrends()
    {
        if ( AccountPrefs.areTwitterDetailsPresent() )
        {
            twitter.setOAuthAccessToken( new AccessToken( AccountPrefs.getTwitterAccessToken(), AccountPrefs.getTwitterAccessTokenSecret() ) );
            Location lastLoc = getLastKnownLocation();
            if ( null != lastLoc )
            {
                twitter4j.Location trendsLoc = getClosestTrendsLocation( new GeoLocation( lastLoc.getLatitude(), lastLoc.getLongitude() ) );
                if ( null != trendsLoc )
                {
                    Trends trends = getTrendsForLocation( trendsLoc.getWoeid() );
                    if ( null != trends )
                    {
                        event = new TwitterTrendsEvent( trends, trendsLoc.getPlaceName(), TRENDS_FOUND );
                    }
                    else
                    {
                        event = getGlobalTrends();
                    }
                }
                else
                {
                    event = getGlobalTrends();
                }
            }
            else
            {
                String countryIso = getSimCountryIso();
                if ( null != countryIso )
                {
                    twitter4j.Location countryLoc = getTrendsLocationForCountry( countryIso );
                    if ( null != countryLoc )
                    {
                        Trends trends = getTrendsForLocation( countryLoc.getWoeid() );
                        if ( null != trends )
                        {
                            event = new TwitterTrendsEvent( trends, countryLoc.getCountryName(), TRENDS_FOUND );
                        }
                        else
                        {
                            event = getGlobalTrends();
                        }
                    }
                    else
                    {
                        event = getGlobalTrends();
                    }
                }
                else
                {
                    event = getGlobalTrends();
                }
            }
        }
        else
        {
            event = new TwitterTrendsEvent( null, null, TWITTER_NOT_LOGGED_IN );
        }
        new Handler( Looper.getMainLooper() ).post( new Runnable()
        {
            @Override
            public void run()
            {
                HashtaggerApp.bus.post( event );
            }
        } );
        mThread.quit();
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
                if ( l.getCountryCode() == getSimCountryIso() )
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

    private String getSimCountryIso()
    {
        String countryIso = null;
        TelephonyManager manager = ( TelephonyManager ) HashtaggerApp.app.getSystemService( Context.TELEPHONY_SERVICE );
        countryIso = manager.getSimCountryIso();
        Helper.debug( null == countryIso ? "null" : countryIso );
        return countryIso;
    }

    private TwitterTrendsEvent getGlobalTrends()
    {
        TwitterTrendsEvent tte;
        Trends globalTrends = getTrendsForLocation( 1 );
        if ( null == globalTrends )
        {
            tte = new TwitterTrendsEvent( null, null, TRENDS_NOT_AVAILABLE );
        }
        else
        {
            tte = new TwitterTrendsEvent( globalTrends, "World", TRENDS_FOUND );
        }
        return tte;
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

    private Location getLastKnownLocation()
    {
        LocationManager locationManager = ( LocationManager ) HashtaggerApp.app.getSystemService( Context.LOCATION_SERVICE );
        return locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
    }

    @Override
    public void onLocationChanged( Location location )
    {

    }

    @Override
    public void onStatusChanged( String provider, int status, Bundle extras )
    {

    }

    @Override
    public void onProviderEnabled( String provider )
    {

    }

    @Override
    public void onProviderDisabled( String provider )
    {

    }
}
