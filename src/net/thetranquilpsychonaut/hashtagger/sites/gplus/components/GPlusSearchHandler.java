package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import android.content.Context;
import android.content.Intent;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;

import java.util.List;

/**
 * Created by itwenty on 5/7/14.
 */
public class GPlusSearchHandler extends SitesSearchHandler
{
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
    public void onReceive( Context context, Intent intent )
    {
        Helper.debug( "onReceive" );
        SearchType searchType = ( SearchType ) intent.getSerializableExtra( SearchType.SEARCH_TYPE_KEY );
        Result resultType = ( Result ) intent.getSerializableExtra( Result.RESULT_KEY );
        if ( resultType == Result.FAILURE )
        {
            sitesSearchListener.onError( searchType );
            return;
        }
        List<Activity> results = GPlusServiceData.SearchData.popSearchResults();
        sitesSearchListener.afterSearching( searchType, results );
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.GPLUS_SEARCH_ACTION;
    }
}
