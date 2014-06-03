package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.TwitterConfig;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.util.List;

/**
 * Created by itwenty on 6/2/14.
 */
public class TwitterTrendsFetcher
{
    private class TrendsTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground( Void... params )
        {
            Twitter twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
            twitter.setOAuthAccessToken( new AccessToken( AccountPrefs.getTwitterAccessToken(), AccountPrefs.getTwitterAccessTokenSecret() ) );
            try
            {
                List<twitter4j.Location> trends = twitter.trends().getAvailableTrends();
                for ( twitter4j.Location location : trends )
                {
                    Helper.debug( location.toString() );
                }
            }
            catch ( TwitterException e )
            {
                e.printStackTrace();
            }
            Helper.debug( String.valueOf( getBestLocation().getAccuracy() ) );
            return null;
        }
    }

    private TrendsTask mTask;

    public TwitterTrendsFetcher()
    {
        mTask = new TrendsTask();
    }

    public void fetchTrends()
    {
        mTask.execute();
    }

    private Location getBestLocation()
    {
        Location gpslocation = getLocationByProvider( LocationManager.GPS_PROVIDER );
        Location networkLocation =
                getLocationByProvider( LocationManager.NETWORK_PROVIDER );
        // if we have only one location available, the choice is easy 
        if ( gpslocation == null )
        {
            Helper.debug( "No GPS Location available." );
            return networkLocation;
        }
        if ( networkLocation == null )
        {
            Helper.debug( "No Network Location available" );
            return gpslocation;
        }
        // a locationupdate is considered 'old' if its older than the configured 
        // update interval. this means, we didn't get a 
        // update from this provider since the last check 
        long old = System.currentTimeMillis() - 1000 * 60 * 60 * 24;
        boolean gpsIsOld = ( gpslocation.getTime() < old );
        boolean networkIsOld = ( networkLocation.getTime() < old );
        // gps is current and available, gps is better than network 
        if ( !gpsIsOld )
        {
            Helper.debug( "Returning current GPS Location" );
            return gpslocation;
        }
        // gps is old, we can't trust it. use network location 
        if ( !networkIsOld )
        {
            Helper.debug( "GPS is old, Network is current, returning network" );
            return networkLocation;
        }
        // both are old return the newer of those two 
        if ( gpslocation.getTime() > networkLocation.getTime() )
        {
            Helper.debug( "Both are old, returning gps(newer)" );
            return gpslocation;
        }
        else
        {
            Helper.debug( "Both are old, returning network(newer)" );
            return networkLocation;
        }
    }

    /**
     * get the last known location from a specific provider (network/gps)
     */
    private Location getLocationByProvider( String provider )
    {
        Location location = null;

        LocationManager locationManager = ( LocationManager ) HashtaggerApp.app
                .getSystemService( Context.LOCATION_SERVICE );
        try
        {
            if ( locationManager.isProviderEnabled( provider ) )
            {
                location = locationManager.getLastKnownLocation( provider );
            }
        }
        catch ( IllegalArgumentException e )
        {
            Helper.debug( "Cannot acces Provider " + provider );
        }
        return location;
    }
}
