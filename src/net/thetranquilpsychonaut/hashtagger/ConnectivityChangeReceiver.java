package net.thetranquilpsychonaut.hashtagger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/24/14.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver
{
    private ArrayList<ConnectivityChangeListener> listeners;

    public ConnectivityChangeReceiver()
    {
        super();
        listeners = new ArrayList<ConnectivityChangeListener>( );
    }

    public void addListener( ConnectivityChangeListener ccl )
    {
        listeners.add( ccl );
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        NetworkInfo info = ( ( ConnectivityManager )context.getSystemService( Context.CONNECTIVITY_SERVICE ) ).getActiveNetworkInfo();
        for( ConnectivityChangeListener ccl : listeners )
        {
            if( info != null && info.isConnected() )
                ccl.onConnected();
            else
                ccl.onDisconnected();
        }
    }
}
