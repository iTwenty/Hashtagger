package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Context;
import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookSearchHandler extends SitesSearchHandler
{
    public FacebookSearchHandler( SitesSearchListener listener )
    {
        super( listener );
    }

    @Override
    protected Intent addExtraParameters( Intent searchIntent )
    {
        return null;
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return null;
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {

    }

    @Override
    public String getSearchActionName()
    {
        return null;
    }
}
