package net.thetranquilpsychonaut.hashtagger;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by itwenty on 2/7/14.
 */
public class HashtaggerApp extends Application
{
    public static HashtaggerApp app;
    public static final Bus bus = new Bus( ThreadEnforcer.ANY );

    public static final String PACKAGE_NAMESPACE = "net.thetranquilpsychonaut.hashtagger";
    public static final String NAMESPACE         = "hashtagger:";
    public static final String HASHTAG_KEY       = NAMESPACE + "hashtag";

    public static final int TOTAL_SITES_COUNT = 3;

    public static final String TWITTER       = "Twitter";
    public static final int    TWITTER_VALUE = 0;

    public static final String GPLUS       = "Google+";
    public static final int    GPLUS_VALUE = 1;

    public static final String FACEBOOK       = "Facebook";
    public static final int    FACEBOOK_VALUE = 2;


    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
    }

    public static boolean isNetworkConnected()
    {
        ConnectivityManager cm = ( ConnectivityManager ) HashtaggerApp.app.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = cm.getActiveNetworkInfo();
        return ( info != null && info.isConnected() );
    }
}
