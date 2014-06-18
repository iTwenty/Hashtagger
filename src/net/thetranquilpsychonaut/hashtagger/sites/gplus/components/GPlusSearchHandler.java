package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.ActivityFeed;
import net.thetranquilpsychonaut.hashtagger.utils.Linkifier;

import java.util.Iterator;

/**
 * Created by itwenty on 5/7/14.
 */
public class GPlusSearchHandler extends SitesSearchHandler
{
    private static Activity newestActivity;

    public GPlusSearchHandler( SitesSearchListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return GPlusService.class;
    }

    @Override
    public boolean isSearchRunning()
    {
        return GPlusService.getIsServiceRunning();
    }

    @Override
    public void cancelCurrentSearch()
    {
        GPlusService.setIsServiceRunning( false );
    }

    @Override
    public void onReceive( Context context, final Intent intent )
    {
        final Handler main = new Handler( Looper.getMainLooper() );
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                final int searchType = intent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
                int resultType = intent.getIntExtra( Result.RESULT_KEY, -1 );
                if ( resultType == Result.FAILURE )
                {
                    main.post( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            sitesSearchListener.onError( searchType );
                        }
                    } );
                    return;
                }
                final ActivityFeed result = ( ActivityFeed ) intent.getSerializableExtra( Result.RESULT_DATA );

                // For initial search, we simply set newestActivity to be the newest from our result list
                if ( searchType == SearchType.INITIAL && !result.getItems().isEmpty() )
                {
                    newestActivity = result.getItems().get( 0 );
                }

                // For newer and timed search, we first remove all activities older
                // than our newest activity. If we still have some left in the end
                // we set the newest of those left to be our newestActivity.
                else if ( searchType == SearchType.NEWER || searchType == SearchType.TIMED )
                {
                    Iterator<Activity> iterator = result.getItems().iterator();
                    while ( iterator.hasNext() )
                    {
                        if ( iterator.next().getPublished().getTime() <= newestActivity.getPublished().getTime() )
                        {
                            iterator.remove();
                        }
                    }
                    if ( !result.getItems().isEmpty() )
                    {
                        newestActivity = result.getItems().get( 0 );
                    }
                }

                for ( Activity activity : result.getItems() )
                // We strip all HTML formatting tags from the text of the activity since parsing HTML in getView causes awful lag in scrolling
                {
                    activity.getObject().setLinkedText( Linkifier.getLinkedGPlusText( activity.getObject().getContent() ) );
                }

                main.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        sitesSearchListener.afterSearching( searchType, result.getItems() );
                    }
                } );
            }

        } ).start();
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.GPLUS_SEARCH_ACTION;
    }
}
