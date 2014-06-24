package net.thetranquilpsychonaut.hashtagger.sites.instagram.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramSearchHandler extends SitesSearchHandler
{
    public InstagramSearchHandler( SitesSearchListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return InstagramService.class;
    }

    @Override
    public boolean isSearchRunning()
    {
        return InstagramService.isSearchRunning();
    }
}
