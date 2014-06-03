package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;

import java.util.List;

/**
 * Created by itwenty on 3/14/14.
 */
public abstract class SitesSearchHandler extends BroadcastReceiver implements SearchActionName
{
    public interface SitesSearchListener
    {
        public void whileSearching( int searchType );

        public void afterSearching( int searchType, List<?> results );

        public void onError( int searchType );
    }

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
        searchIntent = addExtraParameters( searchIntent );
        HashtaggerApp.app.startService( searchIntent );
        sitesSearchListener.whileSearching( searchType );
    }

    protected Intent addExtraParameters( Intent searchIntent )
    {
        return searchIntent;
    }

    protected abstract Class<?> getServiceClass();

    public void registerReceiver()
    {
        IntentFilter filter = new IntentFilter( getSearchActionName() );
        filter.addCategory( Intent.CATEGORY_DEFAULT );
        HashtaggerApp.app.registerReceiver( this, filter );
    }

    public void unregisterReceiver()
    {
        HashtaggerApp.app.unregisterReceiver( this );
    }

    public abstract boolean isSearchRunning();
}
