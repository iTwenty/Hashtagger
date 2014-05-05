package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;

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
