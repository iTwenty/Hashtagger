package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;

import java.util.List;

/**
 * Created by itwenty on 3/14/14.
 */
public abstract class SitesSearchHandler
{
    public interface SitesSearchListener
    {
        public void whileSearching( int searchType );

        public void afterSearching( int searchType, List<?> results );

        public void onError( int searchType );
    }

    // We use this handler to call the methods of SitesSearchListener which
    // must run on UI thread.
    private Handler mainHandler = new Handler( Looper.getMainLooper() );

    protected SitesSearchListener sitesSearchListener;

    public SitesSearchHandler( SitesSearchListener listener )
    {
        sitesSearchListener = listener;
    }

    public void beginSearch( int searchType, String hashtag )
    {
        Intent searchIntent = new Intent( HashtaggerApp.app, getServiceClass() );
        searchIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.SEARCH );
        searchIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        searchIntent.putExtra( HashtaggerApp.HASHTAG_KEY, hashtag );
        HashtaggerApp.app.startService( searchIntent );
        sitesSearchListener.whileSearching( searchType );
    }

    protected abstract Class<?> getServiceClass();

    public void registerReceiver()
    {
        HashtaggerApp.bus.register( this );
    }

    public void unregisterReceiver()
    {
        HashtaggerApp.bus.unregister( this );
    }

    public abstract boolean isSearchRunning();

    protected Handler getMainHandler()
    {
        return this.mainHandler;
    }
}
