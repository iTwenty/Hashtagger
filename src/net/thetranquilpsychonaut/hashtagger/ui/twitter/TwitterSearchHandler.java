package net.thetranquilpsychonaut.hashtagger.ui.twitter;

import android.os.AsyncTask;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.ui.SitesHandler;
import twitter4j.*;

/**
 * Created by itwenty on 3/13/14.
 */
public class TwitterSearchHandler implements StatusListener, SitesHandler
{
    TwitterTask                  twitterTask;
    Twitter                      twitter;
    TwitterStream                twitterStream;
    FilterQuery                  filterQuery;
    String                       hashtag;
    TwitterSearchHandlerListener listener;
    boolean                      isInListeningMode;

    public TwitterSearchHandler()
    {
        this.twitter = new TwitterFactory( HashtaggerApp.CONFIGURATION ).getInstance();
        this.twitterStream = new TwitterStreamFactory( HashtaggerApp.CONFIGURATION ).getInstance();
        if( TwitterUserHandler.isUserLoggedIn() )
        {
            setAccessToken();
        }
        reset();
    }

    public void setAccessToken()
    {
        twitter.setOAuthAccessToken( TwitterUserHandler.getAccessToken() );
        twitterStream.setOAuthAccessToken( TwitterUserHandler.getAccessToken() );
    }

    public void clearAccessToken()
    {
        twitter.setOAuthAccessToken( null );
        twitterStream.setOAuthAccessToken( null );
    }

    public void beginSearch()
    {
        this.twitterTask.execute( this.hashtag );
    }

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
        twitterStream.shutdown();
        reset();
    }

    private void reset()
    {
        twitterTask = new TwitterTask( this );
        filterQuery = new FilterQuery();
        hashtag = null;
        isInListeningMode = false;
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
            try
            {
                if( null == twitterhandler.twitter.getOAuthAccessToken() )
                    throw new TwitterException("");
                result = twitterhandler.twitter.search( query );
            }
            catch ( TwitterException e )
            {
                Helper.debug( "Error while searching  for " + params[0] );
                twitterhandler.listener.onError();
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void aVoid )
        {
            if ( null != result )
            {
                twitterhandler.listener.afterSearching( result.getTweets() );
                twitterhandler.switchToListeningMode();
            }
            else
            {
                Helper.debug( "Result is null" );
                twitterhandler.listener.onError();
            }
        }
    }

    public void switchToListeningMode()
    {
        listener.onBeginStream();
        isInListeningMode = true;
        filterQuery.track( new String[]{ this.hashtag } );
        twitterStream.addListener( this );
        twitterStream.filter( filterQuery );
    }

    public boolean isInListeningMode()
    {
        return isInListeningMode;
    }

    public void pauseSearch()
    {
        if ( !isInListeningMode )
            return;
        twitterStream.cleanUp();
    }

    public void resumeSearch()
    {
        if ( !isInListeningMode )
            return;
        twitterStream.filter( filterQuery );
    }

    @Override
    public void onStatus( Status status )
    {
        listener.onStatus( status );
    }

    @Override
    public void onDeletionNotice( StatusDeletionNotice statusDeletionNotice )
    {

    }

    @Override
    public void onTrackLimitationNotice( int i )
    {

    }

    @Override
    public void onScrubGeo( long l, long l2 )
    {

    }

    @Override
    public void onStallWarning( StallWarning stallWarning )
    {

    }

    @Override
    public void onException( Exception e )
    {
        Helper.debug( "Error while listening" );
        listener.onStreamError();
    }
}
