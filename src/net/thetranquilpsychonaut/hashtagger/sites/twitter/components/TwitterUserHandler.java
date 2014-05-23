package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;

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
        AccountPrefs.removeTwitterDetails();
        sitesUserListener.onUserLoggedOut();
    }

    @Override
    public boolean isUserLoggedIn()
    {
        return AccountPrefs.areTwitterDetailsPresent();
    }

    @Override
    public String getUserName()
    {
        if ( !isUserLoggedIn() )
        {
            throw new RuntimeException( "User must be logged in before prodding user name." );
        }
        return AccountPrefs.getTwitterUserName();
    }
}
