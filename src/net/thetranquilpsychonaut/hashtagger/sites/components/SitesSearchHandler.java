package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SearchActionName;

/**
 * Created by itwenty on 3/14/14.
 */
public abstract class SitesSearchHandler extends BroadcastReceiver implements SearchActionName
{
    public interface SitesSearchListener
    {
        public void whileSearching( SearchType searchType );

        public void afterSearching( SearchType searchType, Bundle resultBundle );

        public void onError( SearchType searchType );
    }

    protected IntentFilter        filter;
    protected SitesSearchListener sitesSearchListener;
    protected String              hashtag;

    public SitesSearchHandler()
    {
        filter = new IntentFilter( getSearchActionName() );
        filter.addCategory( Intent.CATEGORY_DEFAULT );
        HashtaggerApp.app.getApplicationContext().registerReceiver( this, filter );
    }

    public void setHashtag( String hashtag )
    {
        this.hashtag = hashtag;
    }

    public void setSitesSearchListener( SitesSearchListener sitesSearchListener )
    {
        this.sitesSearchListener = sitesSearchListener;
    }

    public void beginSearch( SearchType searchType )
    {
        Intent searchIntent = new Intent( HashtaggerApp.app.getApplicationContext(), getServiceClass() );
        searchIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.SEARCH );
        searchIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
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
