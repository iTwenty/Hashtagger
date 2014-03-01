package net.thetranquilpsychonaut.hashtagger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

/**
 * Created by itwenty on 2/7/14.
 */
public class Helper
{
    public static boolean isNetworkConnected( )
    {
        ConnectivityManager cm = ( ConnectivityManager )HashtaggerApp.app.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = cm.getActiveNetworkInfo( );
        return ( info != null && info.isConnected( ) );
    }
}

