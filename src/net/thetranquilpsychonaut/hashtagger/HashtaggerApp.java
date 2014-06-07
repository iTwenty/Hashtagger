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
    public static Bus           bus;

    public static final String PACKAGE_NAMESPACE = "net.thetranquilpsychonaut.hashtagger";
    public static final String NAMESPACE         = "hashtagger:";


    public static final String HASHTAG_KEY       = NAMESPACE + "hashtag";
    public static final int    TOTAL_SITES_COUNT = 3;


    public static final String TWITTER               = "Twitter";
    public static final int    TWITTER_VALUE         = 0;
    public static final String TWITTER_SEARCH_ACTION = NAMESPACE + "twitter_search_action";
    public static final String TWITTER_LOGIN_ACTION  = NAMESPACE + "twitter_login_action";


    public static final String FACEBOOK               = "Facebook";
    public static final int    FACEBOOK_VALUE         = 1;
    public static final String FACEBOOK_LOGIN_ACTION  = NAMESPACE + "facebook_login_action";
    public static final String FACEBOOK_SEARCH_ACTION = NAMESPACE + "facebook_search_action";


    public static final String GPLUS               = "Google+";
    public static final int    GPLUS_VALUE         = 2;
    public static final String GPLUS_LOGIN_ACTION  = NAMESPACE + "gplus_login_action";
    public static final String GPLUS_SEARCH_ACTION = NAMESPACE + "gplus_search_action";

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        bus = new Bus( ThreadEnforcer.ANY );
    }

    public static boolean isNetworkConnected()
    {
        ConnectivityManager cm = ( ConnectivityManager ) HashtaggerApp.app.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = cm.getActiveNetworkInfo();
        return ( info != null && info.isConnected() );
    }
}
