package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;

/**
 * Created by itwenty on 3/24/14.
 */
public class TwitterUserHandler extends SitesUserHandler
{

    public TwitterUserHandler( SitesUserListener listener )
    {
        super( listener );
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

    @Override
    public String getUserName()
    {
        if ( !isUserLoggedIn() )
            throw new RuntimeException( "User must be logged in before prodding user name." );
        return SharedPreferencesHelper.getTwitterUserName();
    }
}
