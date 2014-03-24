package net.thetranquilpsychonaut.hashtagger;

import android.content.Context;
import android.content.SharedPreferences;
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
public class Helper
{
    public static void debug( String s )
    {
        if( HashtaggerApp.DEBUG )
        {
            Log.d( "twtr", s );
        }
    }
}
