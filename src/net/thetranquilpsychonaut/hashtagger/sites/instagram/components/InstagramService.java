package net.thetranquilpsychonaut.hashtagger.sites.instagram.components;

import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramService extends SitesService
{
    private static boolean isSearchRunning = false;

    @Override
    protected void doSearch( Intent intent )
    {

    }

    @Override
    protected void doAuth( Intent intent )
    {

    }

    public static boolean isSearchRunning()
    {
        return isSearchRunning;
    }
}
