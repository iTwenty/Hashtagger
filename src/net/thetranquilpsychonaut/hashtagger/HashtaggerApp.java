package net.thetranquilpsychonaut.hashtagger;

import android.app.Application;
import android.util.Log;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.ArrayList;

/**
 * Created by itwenty on 2/7/14.
 */
public class HashtaggerApp extends Application
{
    public static final int TIME_DELAY = 5000;
    public static final ArrayList<String> SITES = new ArrayList<String>( );
    public static HashtaggerApp app;
    public static final Bus bus = new Bus( ThreadEnforcer.ANY );
    public static final String OAUTH_CONSUMER_KEY = "DuqA2VDJshEGIBkoJQmQ";
    public static final String OAUTH_CONSUMER_SECRET = "urM01bpqvEyw4ii3Ein59tMaTmG1H7k1RLKwTXQgo";
    public static final String OAUTH_ACCESS_TOKEN = "1644677155-tJl5mEps5YENMpQin5IEiKjSlMjMIIWSPJby3Wu";
    public static final String OAUTH_ACCESS_TOKEN_SECRET = "gUpdkY75TYhPfO85VvZ6Qx6IdHwhtPzgq3hngIx7xfONn";

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
