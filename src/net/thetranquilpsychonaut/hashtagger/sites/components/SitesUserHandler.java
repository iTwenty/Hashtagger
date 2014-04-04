package net.thetranquilpsychonaut.hashtagger.sites.components;

/**
 * Created by itwenty on 3/24/14.
 */
public abstract class SitesUserHandler
{
    public static interface SitesUserListener
    {
        public void onUserLoggedOut();
    }

    protected SitesUserListener sitesUserListener;

    public void setSitesUserListener( SitesUserListener sitesUserListener )
    {
        this.sitesUserListener = sitesUserListener;
    }

    public abstract boolean isUserLoggedIn();

    public abstract String getUserName();

    public abstract void logoutUser();
}
