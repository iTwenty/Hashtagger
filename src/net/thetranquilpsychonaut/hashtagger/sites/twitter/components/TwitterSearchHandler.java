package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Context;
import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import twitter4j.QueryResult;

/**
 * Created by itwenty on 3/13/14.
 */
public class TwitterSearchHandler extends SitesSearchHandler
{
    /*
    max and since ids are used to navigate through the tweets timeline.
    tweet ids are time based i.e later tweets have higher ids than older tweets.
    */

    static long maxId;
    static long sinceId;

    public TwitterSearchHandler( SitesSearchListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return TwitterService.class;
    }

    @Override
    public boolean isSearchRunning()
    {
        return TwitterService.isIsSearchRunning();
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
        QueryResult result = ( QueryResult ) intent.getSerializableExtra( Result.RESULT_DATA );
        sitesSearchListener.afterSearching( searchType, result.getTweets() );
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.TWITTER_SEARCH_ACTION;
    }
}
