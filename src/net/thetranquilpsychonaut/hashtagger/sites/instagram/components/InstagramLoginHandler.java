package net.thetranquilpsychonaut.hashtagger.sites.instagram.components;

import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramLoginHandler extends SitesLoginHandler
{
    public InstagramLoginHandler( SitesLoginListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return InstagramService.class;
    }
}
