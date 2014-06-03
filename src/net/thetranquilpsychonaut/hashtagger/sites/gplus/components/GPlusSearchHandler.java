package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;

import java.util.Iterator;
import java.util.List;

/**
 * Created by itwenty on 5/7/14.
 */
public class GPlusSearchHandler extends SitesSearchHandler
{
    public static  String   nextPageToken;
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
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                final int searchType = intent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
                int resultType = intent.getIntExtra( Result.RESULT_KEY, -1 );
                if ( resultType == Result.FAILURE )
                {
                    sitesSearchListener.onError( searchType );
                    return;
                }
                final List<Activity> results = GPlusData.SearchData.popSearchResults();
                if ( searchType == SearchType.INITIAL && !results.isEmpty() )
                {
                    newestActivity = results.get( 0 );
                }
                else if ( searchType == SearchType.NEWER || searchType == SearchType.TIMED )
                {
                    Iterator<Activity> iterator = results.iterator();
                    while ( iterator.hasNext() )
                    {
                        if ( iterator.next().getPublished().getValue() <= newestActivity.getPublished().getValue() )
                        {
                            iterator.remove();
                        }
                    }
                    if ( !results.isEmpty() )
                    {
                        newestActivity = results.get( 0 );
                    }
                }

                for ( Activity activity : results )
                // We strip all HTML formatting tags from the text of the activity since parsing HTML in getView causes awful lag in scrolling
                {
                    activity.getObject().setOriginalContent( Html.fromHtml( activity.getObject().getContent() ).toString() );
                }

                new Handler( Looper.getMainLooper() ).post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        sitesSearchListener.afterSearching( searchType, results );
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
