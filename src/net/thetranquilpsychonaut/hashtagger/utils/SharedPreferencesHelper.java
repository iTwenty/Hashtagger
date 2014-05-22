package net.thetranquilpsychonaut.hashtagger.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 4/4/14.
 */
public class SharedPreferencesHelper
{
    public static final String LOGIN_SHARED_PREFS              = "shared_prefs";
    public static final String TWITTER_ACCESS_TOKEN_KEY        = "twitter_access_token";
    public static final String TWITTER_ACCESS_TOKEN_SECRET_KEY = "twitter_access_token_secret";
    public static final String TWITTER_USER_NAME_KEY           = "twitter_user_name";
    public static final String FACEBOOK_ACCESS_TOKEN_KEY       = "facebook_access_token";
    public static final String FACEBOOK_USER_NAME_KEY          = "facebook_user_name";
    public static final String GPLUS_ACCESS_TOKEN_KEY          = "gplus_access_token";
    public static final String GPLUS_REFRESH_TOKEN_KEY         = "gplus_refresh_token";
    public static final String GPLUS_USER_NAME_KEY             = "gplus_user_name";

    public static SharedPreferences shared_prefs = HashtaggerApp.app.getSharedPreferences( LOGIN_SHARED_PREFS, Context.MODE_PRIVATE );

    public static final String TWITTER_SITE_KEY         = "site_twitter";
    public static final String FACEBOOK_SITE_KEY        = "site_facebook";
    public static final String GPLUS_SITE_KEY           = "site_gplus";
    public static final String AUTO_UPDATE_INTERVAL_KEY = "auto_update_interval";
    public static final String CLEAR_SEARCH_KEY         = "clear_search";

    public static SharedPreferences default_prefs = PreferenceManager.getDefaultSharedPreferences( HashtaggerApp.app );

    // These values are updated whenever Preferences change in the Settings screen.
    // Helps avoid disk access by keeping them in memory.
    public static boolean twitterActive      = SharedPreferencesHelper.isTwitterActive();
    public static boolean facebookActive     = SharedPreferencesHelper.isFacebookActive();
    public static boolean gPlusActive        = SharedPreferencesHelper.isGPlusActive();
    public static boolean activeSitesChanged = false;
    public static int     autoUpdateInterval = SharedPreferencesHelper.getAutoUpdateInterval();


    /**
     * ************ Twitter ************
     */


    public static void removeTwitterDetails()
    {
        shared_prefs.edit()
                .remove( TWITTER_ACCESS_TOKEN_KEY )
                .remove( TWITTER_ACCESS_TOKEN_SECRET_KEY )
                .remove( TWITTER_USER_NAME_KEY )
                .commit();
    }

    public static boolean areTwitterDetailsPresent()
    {
        return shared_prefs.contains( TWITTER_ACCESS_TOKEN_KEY );
    }

    public static void addTwitterDetails( String accessToken, String accessTokenSecret, String userName )
    {
        shared_prefs.edit()
                .putString( TWITTER_ACCESS_TOKEN_KEY, accessToken )
                .putString( TWITTER_ACCESS_TOKEN_SECRET_KEY, accessTokenSecret )
                .putString( TWITTER_USER_NAME_KEY, userName )
                .commit();
    }

    public static String getTwitterAccessToken()
    {
        return shared_prefs.getString( TWITTER_ACCESS_TOKEN_KEY, "" );
    }

    public static String getTwitterAccessTokenSecret()
    {
        return shared_prefs.getString( TWITTER_ACCESS_TOKEN_SECRET_KEY, "" );
    }

    public static String getTwitterUserName()
    {
        return shared_prefs.getString( TWITTER_USER_NAME_KEY, "" );
    }


     /* ************* Facebook *******************/


    public static void addFacebookDetails( String accessToken, String userName )
    {
        shared_prefs.edit()
                .putString( FACEBOOK_ACCESS_TOKEN_KEY, accessToken )
                .putString( FACEBOOK_USER_NAME_KEY, userName )
                .commit();
    }

    public static void removeFacebookDetails()
    {
        shared_prefs.edit()
                .remove( FACEBOOK_ACCESS_TOKEN_KEY )
                .remove( FACEBOOK_USER_NAME_KEY )
                .commit();
    }

    public static boolean areFacebookDetailsPresent()
    {
        return shared_prefs.contains( FACEBOOK_ACCESS_TOKEN_KEY );
    }

    public static String getFacebookAccessToken()
    {
        return shared_prefs.getString( FACEBOOK_ACCESS_TOKEN_KEY, "" );
    }

    public static String getFacebookUserName()
    {
        return shared_prefs.getString( FACEBOOK_USER_NAME_KEY, "" );
    }


    /**
     * *********************** Google+ **************************
     */


    public static void addGPlusDetails( String accessToken, String refreshToken, String userName )
    {
        shared_prefs.edit()
                .putString( GPLUS_ACCESS_TOKEN_KEY, accessToken )
                .putString( GPLUS_REFRESH_TOKEN_KEY, refreshToken )
                .putString( GPLUS_USER_NAME_KEY, userName )
                .commit();
    }

    public static void removeGPlusDetails()
    {
        shared_prefs.edit()
                .remove( GPLUS_ACCESS_TOKEN_KEY )
                .remove( GPLUS_REFRESH_TOKEN_KEY )
                .remove( GPLUS_USER_NAME_KEY )
                .commit();
    }

    public static boolean areGPlusDetailsPresent()
    {
        return shared_prefs.contains( GPLUS_ACCESS_TOKEN_KEY );
    }

    public static String getGPlusAccessToken()
    {
        return shared_prefs.getString( GPLUS_ACCESS_TOKEN_KEY, "" );
    }

    public static String getGPlusRefreshToken()
    {
        return shared_prefs.getString( GPLUS_REFRESH_TOKEN_KEY, "" );
    }

    public static String getGPlusUserName()
    {
        return shared_prefs.getString( GPLUS_USER_NAME_KEY, "" );
    }


    /**
     * *************Preferences Screen Settings *********************
     */


    public static boolean isTwitterActive()
    {
        return default_prefs.getBoolean( TWITTER_SITE_KEY, true );
    }

    public static boolean isFacebookActive()
    {
        return default_prefs.getBoolean( FACEBOOK_SITE_KEY, true );
    }

    public static boolean isGPlusActive()
    {
        return default_prefs.getBoolean( GPLUS_SITE_KEY, true );
    }

    public static int getAutoUpdateInterval()
    {
        return Integer.parseInt( default_prefs.getString( AUTO_UPDATE_INTERVAL_KEY, "30000" ) );
    }
}
