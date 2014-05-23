package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import android.content.Context;
import android.content.Intent;
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
        return GPlusService.isIsSearchRunning();
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        SearchType searchType = ( SearchType ) intent.getSerializableExtra( SearchType.SEARCH_TYPE_KEY );
        Result resultType = ( Result ) intent.getSerializableExtra( Result.RESULT_KEY );
        if ( resultType == Result.FAILURE )
        {
            sitesSearchListener.onError( searchType );
            return;
        }
        List<Activity> results = GPlusData.SearchData.popSearchResults();
        if ( searchType == searchType.INITIAL && !results.isEmpty() )
        {
            newestActivity = results.get( 0 );
        }
        else if ( searchType == searchType.NEWER || searchType == searchType.TIMED )
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
        // We strip all HTML formatting tags from the text of the activity since parsing HTML in getView causes awful lag in scrolling
        for ( Activity activity : results )
        {
            activity.getObject().setOriginalContent( Html.fromHtml( activity.getObject().getContent() ).toString() );
        }
        sitesSearchListener.afterSearching( searchType, results );
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.GPLUS_SEARCH_ACTION;
    }
}
