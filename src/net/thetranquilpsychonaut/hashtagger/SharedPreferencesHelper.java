package net.thetranquilpsychonaut.hashtagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by itwenty on 4/4/14.
 */
public class SharedPreferencesHelper
{
    public static final String LOGIN_SHARED_PREFS                    = "login_shared_prefs.xml";
    public static final String TWITTER_OAUTH_ACCESS_TOKEN_KEY        = HashtaggerApp.NAMESPACE + "twitter_oauth_access_token";
    public static final String TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY = HashtaggerApp.NAMESPACE + "twitter_oauth_access_token_secret";
    public static final String TWITTER_USER_NAME_KEY                 = HashtaggerApp.NAMESPACE + "twitter_user_name";
    public static final String FACEBOOK_OAUTH_ACCESS_TOKEN_KEY       = HashtaggerApp.NAMESPACE + "facebook_access_token";
    public static final String FACEBOOK_USER_NAME_KEY                = HashtaggerApp.NAMESPACE + "facebook_user_name";

    public static final String TWITTER_SERVICE_KEY       = "pref_service_twitter";
    public static final String FACEBOOK_SERVICE_KEY      = "pref_service_facebook";
    public static final String CLEAR_SEARCH_KEY          = "pref_clear_search";
    public static final String GPLUS_SERVICE_KEY         = "pref_service_gplus";
    public static final String ACTIVES_SITES_CHANGED_KEY = "active_sites_changed";

    public static SharedPreferences login_prefs   = HashtaggerApp.app.getSharedPreferences( LOGIN_SHARED_PREFS, Context.MODE_PRIVATE );
    public static SharedPreferences default_prefs = PreferenceManager.getDefaultSharedPreferences( HashtaggerApp.app );

    /**
     * ****** Twitter *************
     */

    public static void removeTwitterDetails()
    {
        login_prefs.edit()
                .remove( TWITTER_OAUTH_ACCESS_TOKEN_KEY )
                .remove( TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY )
                .remove( TWITTER_USER_NAME_KEY )
                .commit();
    }

    public static boolean areTwitterDetailsPresent()
    {
        return login_prefs.contains( TWITTER_OAUTH_ACCESS_TOKEN_KEY );
    }

    public static void addTwitterDetails( String accessToken, String accessTokenSecret, String userName )
    {
        login_prefs.edit()
                .putString( TWITTER_OAUTH_ACCESS_TOKEN_KEY, accessToken )
                .putString( TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY, accessTokenSecret )
                .putString( TWITTER_USER_NAME_KEY, userName )
                .commit();
    }

    public static String getTwitterOauthAccessToken()
    {
        return login_prefs.getString( TWITTER_OAUTH_ACCESS_TOKEN_KEY, "" );
    }

    public static String getTwitterOauthAccessTokenSecret()
    {
        return login_prefs.getString( TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY, "" );
    }

    public static String getTwitterUserName()
    {
        return login_prefs.getString( TWITTER_USER_NAME_KEY, "" );
    }

    /**
     * ************* Facebook *******************
     */

    public static void addFacebookDetails( String accessToken, String userName )
    {
        login_prefs.edit()
                .putString( FACEBOOK_OAUTH_ACCESS_TOKEN_KEY, accessToken )
                .putString( FACEBOOK_USER_NAME_KEY, userName )
                .commit();
    }

    public static void removeFacebookDetails()
    {
        login_prefs.edit()
                .remove( FACEBOOK_OAUTH_ACCESS_TOKEN_KEY )
                .remove( FACEBOOK_USER_NAME_KEY )
                .commit();
    }

    public static boolean areFacebookDetailsPresent()
    {
        return login_prefs.contains( FACEBOOK_OAUTH_ACCESS_TOKEN_KEY );
    }

    public static String getFacebookOauthAccessToken()
    {
        return login_prefs.getString( FACEBOOK_OAUTH_ACCESS_TOKEN_KEY, "" );
    }

    public static String getFacebookUserName()
    {
        return login_prefs.getString( FACEBOOK_USER_NAME_KEY, "" );
    }

    public static boolean isTwitterServiceActive()
    {
        return default_prefs.getBoolean( TWITTER_SERVICE_KEY, true );
    }

    public static boolean isFacebookServiceActive()
    {
        return default_prefs.getBoolean( FACEBOOK_SERVICE_KEY, true );
    }

    public static boolean isGPlusServiceActive()
    {
        return default_prefs.getBoolean( GPLUS_SERVICE_KEY, true );
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
