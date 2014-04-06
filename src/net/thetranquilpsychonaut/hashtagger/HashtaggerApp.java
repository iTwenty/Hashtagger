package net.thetranquilpsychonaut.hashtagger;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


    public static final String            NAMESPACE                  = "hashtagger:";
    public static final String            TWITTER_KEY                = NAMESPACE + "twitter";
    public static final String            HASHTAG_KEY                = NAMESPACE + "hashtag";
    public static final ArrayList<String> SITES                      = new ArrayList<String>();
    public static final String            OAUTH_CONSUMER_KEY         = "M8ezCrLbz1TyyjIE77muA";
    public static final String            OAUTH_CONSUMER_SECRET      = "CfAivT6J3KGEAML2l8w2OVjBXKTo9lzrFQxFXIPzk";
    public static       Configuration     CONFIGURATION              = null;
    public static final String            OAUTH_VERIFIER_KEY         = "oauth_verifier";
    public static final String            CALLBACK_URL               = "twitter-auth-callback:///";
    public static final int               TWITTER_LOGIN_REQUEST_CODE = 1;
    public static final int               TWITTER_SEARCH_LIMIT       = 20;
    public static final String            TWITTER_SEARCH_ACTION      = NAMESPACE + "twitter_search_action";
    public static final String            TWITTER_LOGIN_ACTION       = NAMESPACE + "twitter_auth_action";
    public static final String            TWITTER_STATUS_KEY         = NAMESPACE + "twitter_status_key";
    public static final String            TWITTER_DIALOG_TAG         = NAMESPACE + "twitter_dialog_tag";
    public static final String            SEARCH_RESULT_LIST_KEY     = NAMESPACE + "twitter_search_result_list";
    public static final String            TWITTER_REQUEST_TOKEN_KEY  = NAMESPACE + "twitter_request_token_key";


    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        CONFIGURATION = new ConfigurationBuilder()
            .setOAuthConsumerKey( OAUTH_CONSUMER_KEY )
            .setOAuthConsumerSecret( OAUTH_CONSUMER_SECRET )
            .build();
        SITES.add( getResources().getString( R.string.str_twitter ) );
        SITES.add( getResources().getString( R.string.str_facebook ) );
        SITES.add( getResources().getString( R.string.str_instagram ) );
    }

    public static boolean isNetworkConnected()
    {
        ConnectivityManager cm = ( ConnectivityManager ) HashtaggerApp.app.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = cm.getActiveNetworkInfo();
        return ( info != null && info.isConnected() );
    }
}
