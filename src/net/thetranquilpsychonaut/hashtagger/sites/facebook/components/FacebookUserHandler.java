package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.utils.SharedPreferencesHelper;

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
        return SharedPreferencesHelper.areFacebookDetailsPresent();
    }

    @Override
    public String getUserName()
    {
        if ( !isUserLoggedIn() )
        {
            throw new RuntimeException( "Must be logged in to facebook before prodding user name" );
        }
        return SharedPreferencesHelper.getFacebookUserName();
    }

    @Override
    public void logoutUser()
    {
        SharedPreferencesHelper.removeFacebookDetails();
        sitesUserListener.onUserLoggedOut();
    }
}
