package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;

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
        return false;
    }

    @Override
    public String getUserName()
    {
        return null;
    }

    @Override
    public void logoutUser()
    {

    }
}
