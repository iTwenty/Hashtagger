package net.thetranquilpsychonaut.hashtagger.ui.twitter;

import android.os.AsyncTask;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.ui.SitesSearchHandler;
import twitter4j.*;

/**
 * Created by itwenty on 3/13/14.
 */
public class TwitterSearchHandler implements SitesSearchHandler
{
    TwitterTask                  twitterTask;
    Twitter                      twitter;
    String                       hashtag;
    TwitterSearchHandlerListener listener;

    public TwitterSearchHandler()
    {
        this.twitter = new TwitterFactory( HashtaggerApp.CONFIGURATION ).getInstance();
        if( TwitterUserHandler.isUserLoggedIn() )
        {
            setAccessToken();
        }
        reset();
    }

    public void setAccessToken()
    {
        twitter.setOAuthAccessToken( TwitterUserHandler.getAccessToken() );
    }

    public void clearAccessToken()
    {
        twitter.setOAuthAccessToken( null );
    }

    public void beginSearch()
    {
        this.twitterTask.execute( this.hashtag );
    }

    public void beginOlderSearch() {}

    public void beginNewerSearch() {}

    public void setListener( TwitterSearchHandlerListener listener )
    {
        this.listener = listener;
    }

    public void setHashtag( String hashtag )
    {
        this.hashtag = hashtag;
    }

    public void destroyCurrentSearch()
    {
        twitterTask.cancel( true );
        reset();
    }

    private void reset()
    {
        twitterTask = new TwitterTask( this );
        hashtag = null;
    }

    private static class TwitterTask extends AsyncTask<String, Void, Void>
    {
        Query                query;
        QueryResult          result;
        TwitterSearchHandler twitterhandler;

        public TwitterTask( TwitterSearchHandler twitterhandler )
        {
            query = new Query();
            this.twitterhandler = twitterhandler;
            result = null;
        }

        @Override
        protected void onPreExecute()
        {
            twitterhandler.listener.whileSearching();
        }

        @Override
        protected Void doInBackground( String... params )
        {
            query.setQuery( params[0] );
            query.setCount( HashtaggerApp.TWITTER_SEARCH_LIMIT );
            try
            {
                if ( null == twitterhandler.twitter.getOAuthAccessToken() )
                    throw new TwitterException( "" );
                result = twitterhandler.twitter.search( query );
            }
            catch ( TwitterException e )
            {
                Helper.debug( "Error while searching  for " + params[0] );
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void aVoid )
        {
            if ( null != result )
            {
                twitterhandler.listener.afterSearching( result.getTweets() );
            }
            else
            {
                Helper.debug( "Result is null" );
                twitterhandler.listener.onError();
            }
        }

        @Override
        protected void onCancelled( Void aVoid )
        {
            Helper.debug( "Search canceled" );
        }
    }
}
