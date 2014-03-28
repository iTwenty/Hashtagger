package net.thetranquilpsychonaut.hashtagger.ui.twitter;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.enums.SearchResult;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.ui.SitesSearchHandler;
import twitter4j.*;

/**
 * Created by itwenty on 3/13/14.
 */
public class TwitterSearchHandler implements SitesSearchHandler
{
    //TwitterTask                  twitterTask;
    TwitterSearchService           twitterSearchService;
    Twitter                        twitter;
    String                         hashtag;
    TwitterSearchHandlerListener   listener;
    Context                        context;
    IntentFilter                   filter;
    TwitterSearchBroadcastReceiver twitterSearchBroadcastReceiver;

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
        switch ( searchType )
        {
            case CURRENT:
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

    public void destroySearch()
    {
        //twitterTask.cancel( true );
        twitterSearchService.stopSelf();
    }

    public void unregisterReceiver()
    {
        context.unregisterReceiver( twitterSearchBroadcastReceiver );
    }

//    private static class TwitterTask extends AsyncTask<String, Void, Void>
//    {
//        Query                query;
//        QueryResult          result;
//        TwitterSearchHandler twitterhandler;
//
//        public TwitterTask( TwitterSearchHandler twitterhandler )
//        {
//            query = new Query();
//            this.twitterhandler = twitterhandler;
//            result = null;
//        }
//
//        @Override
//        protected void onPreExecute()
//        {
//            twitterhandler.listener.whileSearching();
//        }
//
//        @Override
//        protected Void doInBackground( String... params )
//        {
//            query.setQuery( params[0] );
//            query.setCount( HashtaggerApp.TWITTER_SEARCH_LIMIT );
//            try
//            {
//                if ( null == twitterhandler.twitter.getOAuthAccessToken() )
//                    throw new TwitterException( "" );
//                result = twitterhandler.twitter.search( query );
//            }
//            catch ( TwitterException e )
//            {
//                Helper.debug( "Error while searching  for " + params[0] );
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute( Void aVoid )
//        {
//            if ( null != result )
//            {
//                twitterhandler.listener.afterSearching( result.getTweets() );
//            }
//            else
//            {
//                Helper.debug( "Result is null" );
//                twitterhandler.listener.onError();
//            }
//        }
//
//        @Override
//        protected void onCancelled( Void aVoid )
//        {
//            Helper.debug( "Search canceled" );
//        }
//    }

    public static class TwitterSearchService extends IntentService
    {
        public TwitterSearchService()
        {
            super(HashtaggerApp.TWITTER_SEARCH_SERVICE);
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
                    case CURRENT:
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
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction( HashtaggerApp.TWITTER_SEARCH_OVER );
            broadcastIntent.addCategory( Intent.CATEGORY_DEFAULT );
            broadcastIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
            broadcastIntent.putExtra( SearchResult.SEARCH_RESULT_KEY, null == result ? SearchResult.FAILURE : SearchResult.SUCCESS );
            broadcastIntent.putExtra( SearchResult.SEARCH_RESULT_DATA, result );
            sendBroadcast( broadcastIntent );
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
            if( resultType == SearchResult.FAILURE )
            {
                handler.listener.onError( searchType );
                return;
            }
            QueryResult result = ( QueryResult ) intent.getSerializableExtra( SearchResult.SEARCH_RESULT_DATA );
            if ( result.getTweets() != null )
            {
                sinceId = result.getMaxId();
                maxId = result.getSinceId() == 0 ? getLowestId( result ) : result.getSinceId();
            }
            handler.listener.afterSearching( searchType, result.getTweets() );
        }

        private long getLowestId( QueryResult result )
        {
            int last = result.getTweets().size() - 1;
            return result.getTweets().get( last ).getId();
        }
    }
}
