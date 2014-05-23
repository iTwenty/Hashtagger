package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookUserHandler extends SitesUserHandler
{
    public FacebookUserHandler( SitesUserListener listener )
    {
        super( listener );
    }

    @Override
    public boolean isUserLoggedIn()
    {
        return AccountPrefs.areFacebookDetailsPresent();
    }

    @Override
    public String getUserName()
    {
        if ( !isUserLoggedIn() )
        {
            throw new RuntimeException( "Must be logged in to facebook before prodding user name" );
        }
        return AccountPrefs.getFacebookUserName();
    }

    @Override
    public void logoutUser()
    {
        AccountPrefs.removeFacebookDetails();
        sitesUserListener.onUserLoggedOut();
    }
}
