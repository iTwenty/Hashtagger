package net.thetranquilpsychonaut.hashtagger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/24/14.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver
{
    private List<ConnectivityChangeListener> listeners;

    public ConnectivityChangeReceiver()
    {
        super();
        listeners = new ArrayList<ConnectivityChangeListener>();
    }

    public void addListener( ConnectivityChangeListener ccl )
    {
        listeners.add( ccl );
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        //debugIntent( intent, "twtr" );
        NetworkInfo info = ( ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE ) ).getActiveNetworkInfo();
        for ( ConnectivityChangeListener ccl : listeners )
        {
            if ( info != null && info.isConnected() )
            {
                if( HashtaggerApp.PREV_CONNECTED == false )
                    ccl.onConnected();
                HashtaggerApp.PREV_CONNECTED = true;
            }
            else
            {
                if( HashtaggerApp.PREV_CONNECTED == true )
                    ccl.onDisconnected();
                HashtaggerApp.PREV_CONNECTED = false;
            }
        }
    }

    private void debugIntent( Intent intent, String tag )
    {
        Log.v( tag, "action: " + intent.getAction() );
        Log.v( tag, "component: " + intent.getComponent() );
        Bundle extras = intent.getExtras();
        if ( extras != null )
        {
            for ( String key : extras.keySet() )
            {
                Log.v( tag, "key [" + key + "]: " +
                    extras.get( key ) + "\n");
            }
        }
        else
        {
            Log.v( tag, "no extras" );
        }
    }
}
