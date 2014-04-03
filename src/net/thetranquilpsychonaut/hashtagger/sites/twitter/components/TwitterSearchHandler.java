package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.util.ArrayList;

/**
 * Created by itwenty on 3/13/14.
 */
public class TwitterSearchHandler extends SitesSearchHandler
{
    Twitter twitter;

    /*
    max and since ids are used to navigate through the tweets timeline.
    tweet ids are time based i.e later tweets have higher ids than older tweets.
     */

    static long maxId;
    static long sinceId;

    public TwitterSearchHandler()
    {
        super();
        this.twitter = new TwitterFactory( HashtaggerApp.CONFIGURATION ).getInstance();
        if ( TwitterUserHandler.isUserLoggedIn() )
        {
            setAccessToken();
        }
    }

    public void setAccessToken()
    {
        twitter.setOAuthAccessToken( TwitterUserHandler.getAccessToken() );
    }

    public void clearAccessToken()
    {
        twitter.setOAuthAccessToken( null );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return TwitterService.class;
    }

    @Override
    protected Intent addExtraParameters( Intent searchIntent )
    {
        searchIntent.putExtra( HashtaggerApp.TWITTER_KEY, twitter );
        return searchIntent;
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        Helper.debug( "twitter search over" );
        SearchType searchType = ( SearchType ) intent.getSerializableExtra( SearchType.SEARCH_TYPE_KEY );
        Result resultType = ( Result ) intent.getSerializableExtra( Result.RESULT_KEY );
        if ( resultType == Result.FAILURE )
        {
            Helper.debug( "Twitter Search Result : Failure" );
            sitesSearchListener.onError( searchType );
            return;
        }
        QueryResult result = ( QueryResult ) intent.getSerializableExtra( Result.RESULT_DATA );
        Bundle outBundle = new Bundle();
        outBundle.putSerializable( HashtaggerApp.TWITTER_SEARCH_RESULT_LIST_KEY, ( ArrayList<Status> ) result.getTweets() );
        sitesSearchListener.afterSearching( searchType, outBundle );
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.TWITTER_SEARCH_ACTION;
    }
}
