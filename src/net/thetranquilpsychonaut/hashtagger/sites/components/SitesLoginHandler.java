package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 5/16/14.
 */
public abstract class SitesLoginHandler extends BroadcastReceiver implements LoginActionName
{
    public void registerReceiver()
    {
        IntentFilter filter = new IntentFilter( getLoginActionName() );
        filter.addCategory( Intent.CATEGORY_DEFAULT );
        HashtaggerApp.app.registerReceiver( this, filter );
    }

    public void unregisterReceiver()
    {
        HashtaggerApp.app.unregisterReceiver( this );
    }
}
