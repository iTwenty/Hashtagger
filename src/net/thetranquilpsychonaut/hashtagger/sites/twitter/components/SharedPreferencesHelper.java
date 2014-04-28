package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Context;
import android.content.SharedPreferences;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 4/4/14.
 */
public class SharedPreferencesHelper
{
    public static final String SHARED_PREFS                          = "shared_prefs";
    public static final String TWITTER_OAUTH_ACCESS_TOKEN_KEY        = HashtaggerApp.NAMESPACE + "oauth_access_token";
    public static final String TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY = HashtaggerApp.NAMESPACE + "oauth_access_token_secret";
    public static final String TWITTER_USER_NAME_KEY                 = HashtaggerApp.NAMESPACE + "twitter_user_name";
    public static final String FACEBOOK_OAUTH_ACCESS_TOKEN_KEY       = HashtaggerApp.NAMESPACE + "facebook_access_token";
    public static final String FACEBOOK_USER_NAME_KEY                = HashtaggerApp.NAMESPACE + "facebook_user_name";

    public static SharedPreferences prefs = HashtaggerApp.app.getSharedPreferences( SHARED_PREFS, Context.MODE_PRIVATE );


    /**
     * ****** Twitter *************
     */

    public static void removeTwitterDetails()
    {
        prefs.edit()
                .remove( TWITTER_OAUTH_ACCESS_TOKEN_KEY )
                .remove( TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY )
                .remove( TWITTER_USER_NAME_KEY )
                .commit();
    }

    public static boolean areTwitterDetailsPresent()
    {
        return prefs.contains( TWITTER_OAUTH_ACCESS_TOKEN_KEY );
    }

    public static void addTwitterDetails( String accessToken, String accessTokenSecret, String userName )
    {
        prefs.edit()
                .putString( TWITTER_OAUTH_ACCESS_TOKEN_KEY, accessToken )
                .putString( TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY, accessTokenSecret )
                .putString( TWITTER_USER_NAME_KEY, userName )
                .commit();
    }

    public static String getTwitterOauthAccessToken()
    {
        return prefs.getString( TWITTER_OAUTH_ACCESS_TOKEN_KEY, "" );
    }

    public static String getTwitterOauthAccessTokenSecret()
    {
        return prefs.getString( TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY, "" );
    }

    public static String getTwitterUserName()
    {
        return prefs.getString( TWITTER_USER_NAME_KEY, "" );
    }

    /**
     * ************* Facebook *******************
     */

    public static void addFacebookDetails( String accessToken, String userName )
    {
        prefs.edit()
                .putString( FACEBOOK_OAUTH_ACCESS_TOKEN_KEY, accessToken )
                .putString( FACEBOOK_USER_NAME_KEY, userName )
                .commit();
    }

    public static void removeFacebookDetails()
    {
        prefs.edit()
                .remove( FACEBOOK_OAUTH_ACCESS_TOKEN_KEY )
                .remove( FACEBOOK_USER_NAME_KEY )
                .commit();
    }

    public static boolean areFacebookDetailsPresent()
    {
        return prefs.contains( FACEBOOK_OAUTH_ACCESS_TOKEN_KEY );
    }

    public static String getFacebookOauthAccessToken()
    {
        return prefs.getString( FACEBOOK_OAUTH_ACCESS_TOKEN_KEY, "" );
    }

    public static String getFacebookUserName()
    {
        return prefs.getString( FACEBOOK_USER_NAME_KEY, "" );
    }
}
