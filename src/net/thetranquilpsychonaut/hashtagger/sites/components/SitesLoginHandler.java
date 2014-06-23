package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.os.Handler;
import android.os.Looper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 5/16/14.
 */
public abstract class SitesLoginHandler
{
    private Handler mainHandler = new Handler( Looper.getMainLooper() );

    public void registerReceiver()
    {
        HashtaggerApp.bus.register( this );
    }

    public void unregisterReceiver()
    {
        HashtaggerApp.bus.unregister( this );
    }

    protected Handler getMainHandler()
    {
        return this.mainHandler;
    }
}
