package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
        public void whileSearching( SearchType searchType );

        public void afterSearching( SearchType searchType, List<?> results );

        public void onError( SearchType searchType );
    }

    protected IntentFilter        filter;
    protected SitesSearchListener sitesSearchListener;

    public SitesSearchHandler( SitesSearchListener listener )
    {
        filter = new IntentFilter( getSearchActionName() );
        filter.addCategory( Intent.CATEGORY_DEFAULT );
        HashtaggerApp.app.getApplicationContext().registerReceiver( this, filter );
        sitesSearchListener = listener;
    }

    public void beginSearch( SearchType searchType, String hashtag )
    {
        Intent searchIntent = new Intent( HashtaggerApp.app.getApplicationContext(), getServiceClass() );
        searchIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.SEARCH );
        searchIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        searchIntent.putExtra( HashtaggerApp.HASHTAG_KEY, hashtag );
        searchIntent = addExtraParameters( searchIntent );
        HashtaggerApp.app.getApplicationContext().startService( searchIntent );
        sitesSearchListener.whileSearching( searchType );
    }

    protected abstract Intent addExtraParameters( Intent searchIntent );

    protected abstract Class<?> getServiceClass();

    public void unregisterReceiver()
    {
        HashtaggerApp.app.getApplicationContext().unregisterReceiver( this );
    }
}
