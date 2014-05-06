package net.thetranquilpsychonaut.hashtagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by itwenty on 4/4/14.
 */
public class SharedPreferencesHelper
{
    public static final String LOGIN_SHARED_PREFS              = "login_shared_prefs.xml";
    public static final String TWITTER_ACCESS_TOKEN_KEY        = "twitter_access_token";
    public static final String TWITTER_ACCESS_TOKEN_SECRET_KEY = "twitter_access_token_secret";
    public static final String TWITTER_USER_NAME_KEY           = "twitter_user_name";
    public static final String FACEBOOK_ACCESS_TOKEN_KEY       = "facebook_access_token";
    public static final String FACEBOOK_USER_NAME_KEY          = "facebook_user_name";
    public static final String GPLUS_ACCESS_TOKEN_KEY          = "gplus_access_token";
    public static final String GPLUS_REFRESH_TOKEN_KEY         = "gplus_refresh_token";
    public static final String GPLUS_USER_NAME_KEY             = "gplus_user_name";

    public static final String TWITTER_SITE_KEY          = "pref_site_twitter";
    public static final String FACEBOOK_SITE_KEY         = "pref_site_facebook";
    public static final String GPLUS_SITE_KEY            = "pref_site_gplus";
    public static final String CLEAR_SEARCH_KEY          = "pref_clear_search";
    public static final String ACTIVES_SITES_CHANGED_KEY = "active_sites_changed";

    public static SharedPreferences login_prefs   = HashtaggerApp.app.getSharedPreferences( LOGIN_SHARED_PREFS, Context.MODE_PRIVATE );
    public static SharedPreferences default_prefs = PreferenceManager.getDefaultSharedPreferences( HashtaggerApp.app );


    /**
     * ************ Twitter ************
     */


    public static void removeTwitterDetails()
    {
        login_prefs.edit()
                .remove( TWITTER_ACCESS_TOKEN_KEY )
                .remove( TWITTER_ACCESS_TOKEN_SECRET_KEY )
                .remove( TWITTER_USER_NAME_KEY )
                .commit();
    }

    public static boolean areTwitterDetailsPresent()
    {
        return login_prefs.contains( TWITTER_ACCESS_TOKEN_KEY );
    }

    public static void addTwitterDetails( String accessToken, String accessTokenSecret, String userName )
    {
        login_prefs.edit()
                .putString( TWITTER_ACCESS_TOKEN_KEY, accessToken )
                .putString( TWITTER_ACCESS_TOKEN_SECRET_KEY, accessTokenSecret )
                .putString( TWITTER_USER_NAME_KEY, userName )
                .commit();
    }

    public static String getTwitterAccessToken()
    {
        return login_prefs.getString( TWITTER_ACCESS_TOKEN_KEY, "" );
    }

    public static String getTwitterAccessTokenSecret()
    {
        return login_prefs.getString( TWITTER_ACCESS_TOKEN_SECRET_KEY, "" );
    }

    public static String getTwitterUserName()
    {
        return login_prefs.getString( TWITTER_USER_NAME_KEY, "" );
    }


     /* ************* Facebook *******************/


    public static void addFacebookDetails( String accessToken, String userName )
    {
        login_prefs.edit()
                .putString( FACEBOOK_ACCESS_TOKEN_KEY, accessToken )
                .putString( FACEBOOK_USER_NAME_KEY, userName )
                .commit();
    }

    public static void removeFacebookDetails()
    {
        login_prefs.edit()
                .remove( FACEBOOK_ACCESS_TOKEN_KEY )
                .remove( FACEBOOK_USER_NAME_KEY )
                .commit();
    }

    public static boolean areFacebookDetailsPresent()
    {
        return login_prefs.contains( FACEBOOK_ACCESS_TOKEN_KEY );
    }

    public static String getFacebookAccessToken()
    {
        return login_prefs.getString( FACEBOOK_ACCESS_TOKEN_KEY, "" );
    }

    public static String getFacebookUserName()
    {
        return login_prefs.getString( FACEBOOK_USER_NAME_KEY, "" );
    }


    /**
     * *********************** Google+ **************************
     */


    public static void addGPlusDetails( String accessToken, String refreshToken, String userName )
    {
        login_prefs.edit()
                .putString( GPLUS_ACCESS_TOKEN_KEY, accessToken )
                .putString( GPLUS_REFRESH_TOKEN_KEY, refreshToken )
                .putString( GPLUS_USER_NAME_KEY, userName )
                .commit();
    }

    public static void removeGPlusDetails()
    {
        login_prefs.edit()
                .remove( GPLUS_ACCESS_TOKEN_KEY )
                .remove( GPLUS_REFRESH_TOKEN_KEY )
                .remove( GPLUS_USER_NAME_KEY )
                .commit();
    }

    public static boolean areGPlusDetailsPresent()
    {
        return login_prefs.contains( GPLUS_ACCESS_TOKEN_KEY );
    }

    public static String getGPlusAccessToken()
    {
        return login_prefs.getString( GPLUS_ACCESS_TOKEN_KEY, "" );
    }

    public static String getGPlusRefreshToken()
    {
        return login_prefs.getString( GPLUS_REFRESH_TOKEN_KEY, "" );
    }

    public static String getGPlusUserName()
    {
        return login_prefs.getString( GPLUS_USER_NAME_KEY, "" );
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

    public static void setActivesSitesChanged( boolean value )
    {
        default_prefs.edit().putBoolean( ACTIVES_SITES_CHANGED_KEY, value ).commit();
    }

    public static boolean getActiveSitesChanged()
    {
        return default_prefs.getBoolean( ACTIVES_SITES_CHANGED_KEY, false );
    }
}
