package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import twitter4j.auth.AccessToken;

/**
 * Created by itwenty on 3/24/14.
 */
public class TwitterUserHandler extends SitesUserHandler
{
    SitesUserListener sitesUserListener;
    private static AccessToken accessToken;
    private static String      userName;

    public void setSitesUserListener( SitesUserListener tuhl )
    {
        this.sitesUserListener = tuhl;
    }

    @Override
    public void logoutUser()
    {
        SharedPreferencesHelper.removeTwitterDetails();
        sitesUserListener.onUserLoggedOut();
    }

    @Override
    public boolean isUserLoggedIn()
    {
        return SharedPreferencesHelper.areTwitterDetailsPresent();
    }

    public AccessToken getAccessToken()
    {
        if ( !isUserLoggedIn() )
            throw new RuntimeException( "User must be logged in before prodding access token." );
        if ( null == accessToken )
            accessToken = new AccessToken( SharedPreferencesHelper.getTwitterOauthAccessToken(), SharedPreferencesHelper.getTwitterOauthAccessTokenSecret() );
        return accessToken;
    }

    @Override
    public String getUserName()
    {
        if ( !isUserLoggedIn() )
            throw new RuntimeException( "User must be logged in before prodding access token." );
        if ( null == userName )
            userName = SharedPreferencesHelper.getTwitterUserName();
        return userName;
    }
}
