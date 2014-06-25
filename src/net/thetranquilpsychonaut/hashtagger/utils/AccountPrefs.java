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
    public static final String GPLUS_ACCESS_TOKEN_KEY          = "gplus_access_token";
    public static final String GPLUS_REFRESH_TOKEN_KEY         = "gplus_refresh_token";
    public static final String GPLUS_USER_NAME_KEY             = "gplus_user_name";
    public static final String INSTAGRAM_ACCESS_TOKEN_KEY      = "instagram_access_token";
    public static final String INSTAGRAM_USER_NAME_KEY         = "instagram_user_name";
    public static final String FACEBOOK_ACCESS_TOKEN_KEY       = "facebook_access_token";
    public static final String FACEBOOK_USER_NAME_KEY          = "facebook_user_name";

    private static final SharedPreferences getAccountPrefs()
    {
        return HashtaggerApp.app.getSharedPreferences( ACCOUNT_PREFS, Context.MODE_PRIVATE );
    }

    /**
     * ********** Twitter ***********
     */

    public static void removeTwitterDetails()
    {
        getAccountPrefs().edit()
                .remove( TWITTER_ACCESS_TOKEN_KEY )
                .remove( TWITTER_ACCESS_TOKEN_SECRET_KEY )
                .remove( TWITTER_USER_NAME_KEY )
                .commit();
    }

    public static boolean areTwitterDetailsPresent()
    {
        SharedPreferences s = getAccountPrefs();
        return s.contains( TWITTER_ACCESS_TOKEN_KEY ) && s.contains( TWITTER_ACCESS_TOKEN_SECRET_KEY );
    }

    public static void addTwitterDetails( String accessToken, String accessTokenSecret, String userName )
    {
        getAccountPrefs().edit()
                .putString( TWITTER_ACCESS_TOKEN_KEY, accessToken )
                .putString( TWITTER_ACCESS_TOKEN_SECRET_KEY, accessTokenSecret )
                .putString( TWITTER_USER_NAME_KEY, userName )
                .commit();
    }

    public static String getTwitterAccessToken()
    {
        return getAccountPrefs().getString( TWITTER_ACCESS_TOKEN_KEY, "" );
    }

    public static String getTwitterAccessTokenSecret()
    {
        return getAccountPrefs().getString( TWITTER_ACCESS_TOKEN_SECRET_KEY, "" );
    }

    public static String getTwitterUserName()
    {
        return getAccountPrefs().getString( TWITTER_USER_NAME_KEY, "" );
    }

    /**
     * ********************* Google+ *************************
     */

    public static void addGPlusDetails( String accessToken, String refreshToken, String userName )
    {
        getAccountPrefs().edit()
                .putString( GPLUS_ACCESS_TOKEN_KEY, accessToken )
                .putString( GPLUS_REFRESH_TOKEN_KEY, refreshToken )
                .putString( GPLUS_USER_NAME_KEY, userName )
                .commit();
    }

    public static void updateGPlusAccessToken( String accessToken )
    {
        getAccountPrefs().edit()
                .putString( GPLUS_ACCESS_TOKEN_KEY, accessToken )
                .commit();
    }

    public static void removeGPlusDetails()
    {
        getAccountPrefs().edit()
                .remove( GPLUS_ACCESS_TOKEN_KEY )
                .remove( GPLUS_REFRESH_TOKEN_KEY )
                .remove( GPLUS_USER_NAME_KEY )
                .commit();
    }

    public static boolean areGPlusDetailsPresent()
    {
        return getAccountPrefs().contains( GPLUS_ACCESS_TOKEN_KEY );
    }

    public static String getGPlusAccessToken()
    {
        return getAccountPrefs().getString( GPLUS_ACCESS_TOKEN_KEY, "" );
    }

    public static String getGPlusRefreshToken()
    {
        return getAccountPrefs().getString( GPLUS_REFRESH_TOKEN_KEY, "" );
    }

    public static String getGPlusUserName()
    {
        return getAccountPrefs().getString( GPLUS_USER_NAME_KEY, "" );
    }

    /**
     * ************* Instagram *****************
     */

    public static void addInstagramDetails( String token, String userName )
    {
        getAccountPrefs().edit()
                .putString( INSTAGRAM_ACCESS_TOKEN_KEY, token )
                .putString( INSTAGRAM_USER_NAME_KEY, userName )
                .commit();
    }

    public static boolean areInstagramDetailsPresent()
    {
        return getAccountPrefs().contains( INSTAGRAM_ACCESS_TOKEN_KEY );
    }

    public static void removeInstagramDetails()
    {
        getAccountPrefs().edit()
                .remove( INSTAGRAM_ACCESS_TOKEN_KEY )
                .remove( INSTAGRAM_USER_NAME_KEY )
                .commit();
    }

    public static String getInstagramUserName()
    {
        return getAccountPrefs().getString( INSTAGRAM_USER_NAME_KEY, "" );
    }

    public static String getInstagramAccessToken()
    {
        return getAccountPrefs().getString( INSTAGRAM_ACCESS_TOKEN_KEY, "" );
    }

    /* ************* Facebook *******************/

    public static void addFacebookDetails( String accessToken, String userName )
    {
        getAccountPrefs().edit()
                .putString( FACEBOOK_ACCESS_TOKEN_KEY, accessToken )
                .putString( FACEBOOK_USER_NAME_KEY, userName )
                .commit();
    }

    public static void removeFacebookDetails()
    {
        getAccountPrefs().edit()
                .remove( FACEBOOK_ACCESS_TOKEN_KEY )
                .remove( FACEBOOK_USER_NAME_KEY )
                .commit();
    }

    public static boolean areFacebookDetailsPresent()
    {
        return getAccountPrefs().contains( FACEBOOK_ACCESS_TOKEN_KEY );
    }

    public static String getFacebookAccessToken()
    {
        return getAccountPrefs().getString( FACEBOOK_ACCESS_TOKEN_KEY, "" );
    }

    public static String getFacebookUserName()
    {
        return getAccountPrefs().getString( FACEBOOK_USER_NAME_KEY, "" );
    }
}
