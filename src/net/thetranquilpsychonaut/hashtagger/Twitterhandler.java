package net.thetranquilpsychonaut.hashtagger;

import android.os.AsyncTask;
import twitter4j.*;

/**
 * Created by itwenty on 3/13/14.
 */
public class Twitterhandler implements StatusListener, SitesHandler
{
    TwitterTask            twitterTask;
    TwitterStream          twitterStream;
    FilterQuery            filterQuery;
    String                 hashtag;
    TwitterHandlerListener listener;
    boolean                isInListeningMode;

    public Twitterhandler()
    {
        reset();
    }

    public void beginSearch()
    {
        this.twitterTask.execute( this.hashtag );
    }

    public void setListener( TwitterHandlerListener listener )
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
        twitterStream = new TwitterStreamFactory( HashtaggerApp.CONFIGURATION ).getInstance();
        filterQuery = new FilterQuery();
        listener = null;
        hashtag = null;
        isInListeningMode = false;
    }

    static class TwitterTask extends AsyncTask<String, Void, QueryResult>
    {
        Twitter        twitter;
        Query          query;
        QueryResult    result;
        Twitterhandler twitterhandler;

        public TwitterTask( Twitterhandler twitterhandler )
        {
            twitter = new TwitterFactory( HashtaggerApp.CONFIGURATION ).getInstance();
            query = new Query();
            this.twitterhandler = twitterhandler;
            result = null;
        }

        @Override
        protected void onPreExecute()
        {
            twitterhandler.listener.onPreExecute();
        }

        @Override
        protected QueryResult doInBackground( String... params )
        {
            query.setQuery( params[0] );
            try
            {
                result = twitter.search( query );
            }
            catch ( TwitterException e )
            {
                e.printStackTrace();
            }
            finally
            {
                return result;
            }
        }

        @Override
        protected void onPostExecute( QueryResult queryResult )
        {
            if ( null != queryResult )
            {
                twitterhandler.listener.onPostExecute( queryResult.getTweets() );
                twitterhandler.switchToListeningMode();
            }
            else
            {
                twitterhandler.listener.onError();
            }
        }
    }

    public void switchToListeningMode()
    {
        listener.onSwitchToListener();
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

    }
}
