package net.thetranquilpsychonaut.hashtagger;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

/**
 * Created by itwenty on 2/7/14.
 */
public class HashtaggerApp extends Application
{
    public static final ArrayList<String> SITES = new ArrayList<String>( );
    public static HashtaggerApp app;
    public static  boolean PREV_CONNECTED = false;
    public static final Bus bus = new Bus( ThreadEnforcer.ANY );
    public static final String OAUTH_CONSUMER_KEY = "DuqA2VDJshEGIBkoJQmQ";
    public static final String OAUTH_CONSUMER_SECRET = "urM01bpqvEyw4ii3Ein59tMaTmG1H7k1RLKwTXQgo";
    public static final String OAUTH_ACCESS_TOKEN = "1644677155-tJl5mEps5YENMpQin5IEiKjSlMjMIIWSPJby3Wu";
    public static final String OAUTH_ACCESS_TOKEN_SECRET = "gUpdkY75TYhPfO85VvZ6Qx6IdHwhtPzgq3hngIx7xfONn";


    public static boolean isNetworkConnected()
    {
        ConnectivityManager cm = ( ConnectivityManager )HashtaggerApp.app.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = cm.getActiveNetworkInfo();
        return ( info != null && info.isConnected() );
    }

    public static Configuration getTwitterConfiguration()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder()
            .setOAuthConsumerKey( HashtaggerApp.OAUTH_CONSUMER_KEY )
            .setOAuthConsumerSecret( HashtaggerApp.OAUTH_CONSUMER_SECRET )
            .setOAuthAccessToken( HashtaggerApp.OAUTH_ACCESS_TOKEN )
            .setOAuthAccessTokenSecret( HashtaggerApp.OAUTH_ACCESS_TOKEN_SECRET );
        return cb.build();
    }

    @Override
    public void onCreate( )
    {
        super.onCreate( );
        app = this;
        PREV_CONNECTED = isNetworkConnected();
        Log.d( "twtr", String.valueOf( PREV_CONNECTED ) );
        SITES.add( getResources( ).getString( R.string.str_twitter ) );
        SITES.add( getResources( ).getString( R.string.str_facebook ) );
        SITES.add( getResources( ).getString( R.string.str_instagram ) );
    }
}
