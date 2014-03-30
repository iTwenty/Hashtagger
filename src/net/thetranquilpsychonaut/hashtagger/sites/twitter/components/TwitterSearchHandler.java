package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.enums.SearchResult;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import twitter4j.*;

import java.util.List;

/**
 * Created by itwenty on 3/13/14.
 */
public class TwitterSearchHandler implements SitesSearchHandler
{
    TwitterSearchService           twitterSearchService;
    Twitter                        twitter;
    String                         hashtag;
    TwitterSearchHandlerListener   listener;
    Context                        context;
    IntentFilter                   filter;
    TwitterSearchBroadcastReceiver twitterSearchBroadcastReceiver;

    /*
    max and since ids are used to navigate through the tweets timeline.
    tweet ids are time based i.e later tweets have higher ids than older tweets.
     */
    private static final String MAX_ID   = "maxId";
    private static final String SINCE_ID = "sinceId";
    private static long maxId;
    private static long sinceId;

    public TwitterSearchHandler()
    {
        this.context = HashtaggerApp.app.getApplicationContext();
        twitterSearchBroadcastReceiver = new TwitterSearchBroadcastReceiver( this );
        this.twitter = new TwitterFactory( HashtaggerApp.CONFIGURATION ).getInstance();
        filter = new IntentFilter( HashtaggerApp.TWITTER_SEARCH_OVER );
        filter.addCategory( Intent.CATEGORY_DEFAULT );
        context.registerReceiver( twitterSearchBroadcastReceiver, filter );
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

    public void beginSearch( SearchType searchType )
    {
        Intent serviceIntent = new Intent( context, TwitterSearchService.class );
        serviceIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        serviceIntent.putExtra( HashtaggerApp.TWITTER_KEY, twitter );
        serviceIntent.putExtra( HashtaggerApp.HASHTAG_KEY, this.hashtag );
        /*
        for our initial search we dont pass in either max or since id.
        Older search retrieves tweets with ids lower than the maxId we pass it
        Newer search retrieves tweets with ids higher than the sinceId we pass it
         */
        switch ( searchType )
        {
            case INITIAL:
                break;
            case OLDER:
                serviceIntent.putExtra( MAX_ID, maxId );
                break;
            case NEWER:
                serviceIntent.putExtra( SINCE_ID, sinceId );
                break;
        }
        context.startService( serviceIntent );
        listener.whileSearching( searchType );
    }

    public void setListener( TwitterSearchHandlerListener listener )
    {
        this.listener = listener;
    }

    public void setHashtag( String hashtag )
    {
        this.hashtag = hashtag;
    }

    public void unregisterReceiver()
    {
        context.unregisterReceiver( twitterSearchBroadcastReceiver );
    }

    public static class TwitterSearchService extends IntentService
    {
        public TwitterSearchService()
        {
            super( HashtaggerApp.TWITTER_SEARCH_SERVICE );
        }

        @Override
        protected void onHandleIntent( Intent intent )
        {
            Helper.debug( "twitter search intent" );
            final SearchType searchType = ( SearchType ) intent.getSerializableExtra( SearchType.SEARCH_TYPE_KEY );
            final Twitter twitter = ( Twitter ) intent.getSerializableExtra( HashtaggerApp.TWITTER_KEY );
            final String hashtag = ( String ) intent.getSerializableExtra( HashtaggerApp.HASHTAG_KEY );
            Query query = new Query( hashtag );
            QueryResult result = null;
            query.setCount( HashtaggerApp.TWITTER_SEARCH_LIMIT );
            try
            {
                if ( null == twitter.getOAuthAccessToken() )
                    throw new TwitterException( "" );
                switch ( searchType )
                {
                    case INITIAL:
                        break;
                    case OLDER:
                        query.setMaxId( intent.getLongExtra( MAX_ID, -1 ) );
                    case NEWER:
                        query.setSinceId( intent.getLongExtra( SINCE_ID, -1 ) );
                }
                result = twitter.search( query );
            }
            catch ( TwitterException e )
            {
                Helper.debug( "Error while searching for " + hashtag );
            }
            SearchResult searchResult = null == result ? SearchResult.FAILURE : SearchResult.SUCCESS;
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction( HashtaggerApp.TWITTER_SEARCH_OVER );
            broadcastIntent.addCategory( Intent.CATEGORY_DEFAULT );
            broadcastIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
            broadcastIntent.putExtra( SearchResult.SEARCH_RESULT_KEY, searchResult );
            if ( searchResult == SearchResult.SUCCESS )
            {
                broadcastIntent.putExtra( SearchResult.SEARCH_RESULT_DATA, result );
                if ( result.getTweets() != null )
                {
                    /*
                    if our current search is the initial one, we set both the max and since ids for subsquent searches.
                    if our current search is older, we don't want it to change the sinceId for our next newer search.
                    if our current search is newer, we don't want it to change the maxId for our next older search.
                     */
                    if ( searchType != SearchType.OLDER )
                        sinceId = result.getMaxId();
                    if ( searchType != SearchType.NEWER )
                        maxId = result.getSinceId() == 0 ? getLowestId( result.getTweets() ) : result.getSinceId();

                }
            }
            sendBroadcast( broadcastIntent );
        }

        private long getLowestId( List<Status> list )
        {
            return list.get( list.size() - 1 ).getId();
        }
    }

    private static class TwitterSearchBroadcastReceiver extends BroadcastReceiver
    {
        TwitterSearchHandler handler;

        public TwitterSearchBroadcastReceiver( TwitterSearchHandler handler )
        {
            this.handler = handler;
        }

        @Override
        public void onReceive( Context context, Intent intent )
        {
            Helper.debug( "twitter search over" );
            SearchType searchType = ( SearchType ) intent.getSerializableExtra( SearchType.SEARCH_TYPE_KEY );
            SearchResult resultType = ( SearchResult ) intent.getSerializableExtra( SearchResult.SEARCH_RESULT_KEY );
            if ( resultType == SearchResult.FAILURE )
            {
                handler.listener.onError( searchType );
                return;
            }
            QueryResult result = ( QueryResult ) intent.getSerializableExtra( SearchResult.SEARCH_RESULT_DATA );
            // In case the search was for older results, we remove the newest one as maxId parameter is inclusive
            // and causes tweet to repeat.
            List<Status> resultList = result.getTweets();
            if ( searchType == SearchType.OLDER )
            {
                resultList = resultList.subList( 1, resultList.size() );
            }
            handler.listener.afterSearching( searchType, resultList );
        }
    }
}
