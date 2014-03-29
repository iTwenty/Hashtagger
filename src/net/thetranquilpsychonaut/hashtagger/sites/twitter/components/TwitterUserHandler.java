package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import twitter4j.auth.AccessToken;

/**
 * Created by itwenty on 3/24/14.
 */
public class TwitterUserHandler implements SitesUserHandler
{
    TwitterUserHandlerListener listener;
    private static AccessToken accessToken;
    private static String      userName;

    public void setListener( TwitterUserHandlerListener tuhl )
    {
        this.listener = tuhl;
    }

    @Override
    public void logoutUser()
    {
        HashtaggerApp.prefs.edit()
            .remove( HashtaggerApp.TWITTER_OAUTH_ACCESS_TOKEN_KEY )
            .remove( HashtaggerApp.TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY )
            .remove( HashtaggerApp.USER_KEY )
            .commit();
        listener.onUserLoggedOut();
    }

    public static boolean isUserLoggedIn()
    {
        return HashtaggerApp.prefs.contains( HashtaggerApp.TWITTER_OAUTH_ACCESS_TOKEN_KEY );
    }

    public static AccessToken getAccessToken()
    {
        if ( !isUserLoggedIn() )
            throw new RuntimeException( "User must be logged in before prodding access token." );
        if ( null == accessToken )
            accessToken = new AccessToken(
                HashtaggerApp.prefs.getString( HashtaggerApp.TWITTER_OAUTH_ACCESS_TOKEN_KEY, "" ),
                HashtaggerApp.prefs.getString( HashtaggerApp.TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY, "" ) );
        return accessToken;
    }

    public static String getUserName()
    {
        if ( !isUserLoggedIn() )
            throw new RuntimeException( "User must be logged in before prodding access token." );
        if ( null == userName )
            userName = HashtaggerApp.prefs.getString( HashtaggerApp.USER_KEY, "" );
        return userName;
    }
}
