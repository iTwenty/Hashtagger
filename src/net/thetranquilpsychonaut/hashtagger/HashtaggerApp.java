package net.thetranquilpsychonaut.hashtagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    public static HashtaggerApp app;

    public static final boolean DEBUG = true;

    public static final String            SHARED_PREFS                          = "net.thetranquilpsychonaut.hashtagger.shared_prefs";
    public static final ArrayList<String> SITES                                 = new ArrayList<String>();
    public static       boolean           PREV_CONNECTED                        = false;
    public static final Bus               bus                                   = new Bus( ThreadEnforcer.ANY );
    public static final String            OAUTH_CONSUMER_KEY                    = "M8ezCrLbz1TyyjIE77muA";
    public static final String            OAUTH_CONSUMER_SECRET                 = "CfAivT6J3KGEAML2l8w2OVjBXKTo9lzrFQxFXIPzk";
    public static       Configuration     CONFIGURATION                         = null;
    public static final String            TWITTER_OAUTH_ACCESS_TOKEN_KEY        = "oauth_access_token";
    public static final String            TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY = "oauth_access_token_secret";
    public static final String            AUTH_URL_KEY                          = "auth_url";
    public static final String            OAUTH_VERIFIER_KEY                    = "oauth_verifier";
    public static final String            OAUTH_DENIED_KEY                      = "denied";
    public static final String            CALLBACK_URL                          = "twitter-auth-callback:///";
    public static final int               TWITTER_REQUEST_CODE                  = 1;
    public static       SharedPreferences prefs                                 = null;
    public static final String            USER_KEY                              = "user";

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        CONFIGURATION = new ConfigurationBuilder()
            .setOAuthConsumerKey( OAUTH_CONSUMER_KEY )
            .setOAuthConsumerSecret( OAUTH_CONSUMER_SECRET ).build();
        PREV_CONNECTED = isNetworkConnected();
        SITES.add( getResources().getString( R.string.str_twitter ) );
        SITES.add( getResources().getString( R.string.str_facebook ) );
        SITES.add( getResources().getString( R.string.str_instagram ) );
        prefs = getSharedPreferences( SHARED_PREFS, Context.MODE_PRIVATE );
    }

    public static boolean isNetworkConnected()
    {
        ConnectivityManager cm = ( ConnectivityManager ) HashtaggerApp.app.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = cm.getActiveNetworkInfo();
        return ( info != null && info.isConnected() );
    }
}
