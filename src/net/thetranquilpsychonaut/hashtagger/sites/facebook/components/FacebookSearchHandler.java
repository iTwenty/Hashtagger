package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Context;
import android.content.Intent;
import facebook4j.Paging;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;

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
//        for ( Post post : results )
//        {
//            if ( !isDisplayablePost( post ) )
//                results.remove( post );
//        }
        sitesSearchListener.afterSearching( searchType, results );
    }

    private boolean isDisplayablePost( Post post )
    {
        String statustType = post.getStatusType();
        if ( null == statustType )
        {
            return true;
        }
        if ( "created_note".equals( statustType )
                || "created_group".equals( statustType )
                || "created_event".equals( statustType )
                || "app_created_story".equals( statustType )
                || "tagged_in_photo".equals( statustType )
                || "approved_friend".equals( statustType ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.FACEBOOK_SEARCH_ACTION;
    }
}
