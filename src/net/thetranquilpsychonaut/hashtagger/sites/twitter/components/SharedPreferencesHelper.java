package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Context;
import android.content.SharedPreferences;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 4/4/14.
 */
public class SharedPreferencesHelper
{
    public static final String            SHARED_PREFS                          = HashtaggerApp.NAMESPACE + "shared_prefs";
    public static final String            TWITTER_OAUTH_ACCESS_TOKEN_KEY        = HashtaggerApp.NAMESPACE + "oauth_access_token";
    public static final String            TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY = HashtaggerApp.NAMESPACE + "oauth_access_token_secret";
    public static final String            TWITTER_USER_NAME_KEY                 = HashtaggerApp.NAMESPACE + "twitter_user_name";

    public static SharedPreferences prefs = HashtaggerApp.app.getSharedPreferences( SHARED_PREFS, Context.MODE_PRIVATE );

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

}
