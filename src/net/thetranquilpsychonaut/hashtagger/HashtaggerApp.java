package net.thetranquilpsychonaut.hashtagger;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by itwenty on 2/7/14.
 */
public class HashtaggerApp extends Application
{
    public static HashtaggerApp app;

    public static final boolean DEBUG = true;

    public static final String TWITTER                      = "Twitter";
    public static final String FACEBOOK                     = "Facebook";
    public static final String PACKAGE_NAMESPACE            = "net.thetranquilpsychonaut.hashtagger";
    public static final String NAMESPACE                    = "hashtagger:";
    public static final String TWITTER_OAUTH_VERIFIER_KEY   = "oauth_verifier";
    public static final String TWITTER_CALLBACK_URL         = "twitter-login-callback:///";
    public static final String FACEBOOK_CALLBACK_URL        = "http://localhost/";
    public static final String FACEBOOK_CODE_KEY            = "code";
    public static final int    TWITTER_LOGIN_REQUEST_CODE   = 1;
    public static final int    TWITTER_SEARCH_LIMIT         = 20;
    public static final String HASHTAG_KEY                  = NAMESPACE + "hashtag";
    public static final String TWITTER_KEY                  = NAMESPACE + "twitter";
    public static final String FACEBOOK_KEY                 = NAMESPACE + "facebook";
    public static final String TWITTER_SEARCH_ACTION        = NAMESPACE + "twitter_search_action";
    public static final String TWITTER_LOGIN_ACTION         = NAMESPACE + "twitter_login_action";
    public static final String TWITTER_REQUEST_TOKEN_KEY    = NAMESPACE + "twitter_request_token_key";
    public static final String FACEBOOK_LOGIN_ACTION        = NAMESPACE + "facebook_login_action";
    public static final String FACEBOOK_SEARCH_ACTION       = NAMESPACE + "facebook_search_action";
    public static final String FACEBOOK_PROFILE_PICTURE_URL = "http://graph.facebook.com/%s/picture?type=square";
    public static final int    FACEBOOK_LOGIN_REQUEST_CODE  = 2;
    public static final int    CACHE_DURATION_MS            = 1000 * 60 * 60; // One hour
    public static final int    TWITTER_POSITION             = 0;
    public static final int    FACEBOOK_POSITION            = 1;
    public static final int    TOTAL_SITES_COUNT            = 2;

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
