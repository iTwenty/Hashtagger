package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import facebook4j.Paging;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;

import java.util.Iterator;
import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookSearchHandler extends SitesSearchHandler
{

    /*
    * Facebook post search results are navigated using pages.
    */
    static Paging<Post> newestPage;
    static Paging<Post> oldestPage;


    public FacebookSearchHandler( SitesSearchListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return FacebookService.class;
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
        List<Post> results = ( List<Post> ) intent.getSerializableExtra( Result.RESULT_DATA );
        Iterator<Post> iterator = results.iterator();
        while ( iterator.hasNext() )
        {
            if ( TextUtils.isEmpty( iterator.next().getMessage() ) )
            {
                iterator.remove();
            }
        }
        sitesSearchListener.afterSearching( searchType, results );
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.FACEBOOK_SEARCH_ACTION;
    }
}
