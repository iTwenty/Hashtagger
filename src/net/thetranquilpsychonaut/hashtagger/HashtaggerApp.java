package net.thetranquilpsychonaut.hashtagger;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by itwenty on 2/7/14.
 */
public class HashtaggerApp extends Application
{
    public static final boolean DEBUG = true;
    public static final ArrayList<String> SITES = new ArrayList<String>( );
    private static HashtaggerApp app;

    @Override
    public void onCreate( )
    {
        super.onCreate( );
        SITES.add( getResources( ).getString( R.string.str_twitter ) );
        SITES.add( getResources( ).getString( R.string.str_facebook ) );
        SITES.add( getResources( ).getString( R.string.str_instagram ) );
        app = this;
    }
}
