package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.utils.SharedPreferencesHelper;

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
        return SharedPreferencesHelper.areGPlusDetailsPresent();
    }

    @Override
    public String getUserName()
    {
        if ( !isUserLoggedIn() )
        {
            throw new RuntimeException( "Must be logged in to Google+ before prodding user name" );
        }
        return SharedPreferencesHelper.getGPlusUserName();
    }

    @Override
    public void logoutUser()
    {
        SharedPreferencesHelper.removeGPlusDetails();
        sitesUserListener.onUserLoggedOut();
    }
}
