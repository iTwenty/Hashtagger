package net.thetranquilpsychonaut.hashtagger.utils;

import android.content.Context;
import android.content.SharedPreferences;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 4/4/14.
 */
public final class AccountPrefs
{
    public static final String ACCOUNT_PREFS                   = "account_prefs";
    public static final String TWITTER_ACCESS_TOKEN_KEY        = "twitter_access_token";
    public static final String TWITTER_ACCESS_TOKEN_SECRET_KEY = "twitter_access_token_secret";
    public static final String TWITTER_USER_NAME_KEY           = "twitter_user_name";
    public static final String FACEBOOK_ACCESS_TOKEN_KEY       = "facebook_access_token";
    public static final String FACEBOOK_USER_NAME_KEY          = "facebook_user_name";
    public static final String GPLUS_ACCESS_TOKEN_KEY          = "gplus_access_token";
    public static final String GPLUS_REFRESH_TOKEN_KEY         = "gplus_refresh_token";
    public static final String GPLUS_USER_NAME_KEY             = "gplus_user_name";

    private static final SharedPreferences account_prefs = HashtaggerApp.app.getSharedPreferences( ACCOUNT_PREFS, Context.MODE_PRIVATE );

    /**
     * ********** Twitter ***********
     */

    public static void removeTwitterDetails()
    {
        account_prefs.edit()
                .remove( TWITTER_ACCESS_TOKEN_KEY )
                .remove( TWITTER_ACCESS_TOKEN_SECRET_KEY )
                .remove( TWITTER_USER_NAME_KEY )
                .commit();
    }

    public static boolean areTwitterDetailsPresent()
    {
        return account_prefs.contains( TWITTER_ACCESS_TOKEN_KEY );
    }

    public static void addTwitterDetails( String accessToken, String accessTokenSecret, String userName )
    {
        account_prefs.edit()
                .putString( TWITTER_ACCESS_TOKEN_KEY, accessToken )
                .putString( TWITTER_ACCESS_TOKEN_SECRET_KEY, accessTokenSecret )
                .putString( TWITTER_USER_NAME_KEY, userName )
                .commit();
    }

    public static String getTwitterAccessToken()
    {
        return account_prefs.getString( TWITTER_ACCESS_TOKEN_KEY, "" );
    }

    public static String getTwitterAccessTokenSecret()
    {
        return account_prefs.getString( TWITTER_ACCESS_TOKEN_SECRET_KEY, "" );
    }

    public static String getTwitterUserName()
    {
        return account_prefs.getString( TWITTER_USER_NAME_KEY, "" );
    }

    /* ************* Facebook *******************/

    public static void addFacebookDetails( String accessToken, String userName )
    {
        account_prefs.edit()
                .putString( FACEBOOK_ACCESS_TOKEN_KEY, accessToken )
                .putString( FACEBOOK_USER_NAME_KEY, userName )
                .commit();
    }

    public static void removeFacebookDetails()
    {
        account_prefs.edit()
                .remove( FACEBOOK_ACCESS_TOKEN_KEY )
                .remove( FACEBOOK_USER_NAME_KEY )
                .commit();
    }

    public static boolean areFacebookDetailsPresent()
    {
        return account_prefs.contains( FACEBOOK_ACCESS_TOKEN_KEY );
    }

    public static String getFacebookAccessToken()
    {
        return account_prefs.getString( FACEBOOK_ACCESS_TOKEN_KEY, "" );
    }

    public static String getFacebookUserName()
    {
        return account_prefs.getString( FACEBOOK_USER_NAME_KEY, "" );
    }

    /**
     * ********************* Google+ *************************
     */

    public static void addGPlusDetails( String accessToken, String refreshToken, String userName )
    {
        account_prefs.edit()
                .putString( GPLUS_ACCESS_TOKEN_KEY, accessToken )
                .putString( GPLUS_REFRESH_TOKEN_KEY, refreshToken )
                .putString( GPLUS_USER_NAME_KEY, userName )
                .commit();
    }

    public static void removeGPlusDetails()
    {
        account_prefs.edit()
                .remove( GPLUS_ACCESS_TOKEN_KEY )
                .remove( GPLUS_REFRESH_TOKEN_KEY )
                .remove( GPLUS_USER_NAME_KEY )
                .commit();
    }

    public static boolean areGPlusDetailsPresent()
    {
        return account_prefs.contains( GPLUS_ACCESS_TOKEN_KEY );
    }

    public static String getGPlusAccessToken()
    {
        return account_prefs.getString( GPLUS_ACCESS_TOKEN_KEY, "" );
    }

    public static String getGPlusRefreshToken()
    {
        return account_prefs.getString( GPLUS_REFRESH_TOKEN_KEY, "" );
    }

    public static String getGPlusUserName()
    {
        return account_prefs.getString( GPLUS_USER_NAME_KEY, "" );
    }
}
