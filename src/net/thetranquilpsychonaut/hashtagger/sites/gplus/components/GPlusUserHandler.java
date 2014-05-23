package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusUserHandler extends SitesUserHandler
{
    public GPlusUserHandler( SitesUserListener listener )
    {
        super( listener );
    }

    @Override
    public boolean isUserLoggedIn()
    {
        return AccountPrefs.areGPlusDetailsPresent();
    }

    @Override
    public String getUserName()
    {
        if ( !isUserLoggedIn() )
        {
            throw new RuntimeException( "Must be logged in to Google+ before prodding user name" );
        }
        return AccountPrefs.getGPlusUserName();
    }

    @Override
    public void logoutUser()
    {
        AccountPrefs.removeGPlusDetails();
        sitesUserListener.onUserLoggedOut();
    }
}
