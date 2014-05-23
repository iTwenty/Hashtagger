package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.os.AsyncTask;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.TwitterConfig;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetEvent;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by itwenty on 5/11/14.
 */
public class TwitterAction
{
    public void executeReplyAction( String reply, long inReplyToUserId )
    {
        new TwitterReplyTask( reply, inReplyToUserId ).execute();
    }

    public void executeRetweetAction( long retweetId, int position )
    {
        new TwitterRetweetTask( retweetId, position ).execute();
    }

    public void executeFavoriteAction( long tweetId, boolean isFavorited, int position )
    {
        new TwitterFavoriteTask( tweetId, isFavorited, position ).execute();
    }

    private static class TwitterReplyTask extends AsyncTask<Void, Void, Void>
    {
        Twitter twitter;
        String  reply;
        long    inReplyToUserId;
        boolean success = false;

        public TwitterReplyTask( String reply, long inReplyToUserId )
        {
            twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
            twitter.setOAuthAccessToken( new AccessToken( AccountPrefs.getTwitterAccessToken(), AccountPrefs.getTwitterAccessTokenSecret() ) );
            this.reply = reply;
            this.inReplyToUserId = inReplyToUserId;
        }

        @Override
        protected Void doInBackground( Void... params )
        {
            try
            {
                twitter.updateStatus( new StatusUpdate( reply ).inReplyToStatusId( inReplyToUserId ) );
                success = true;
            }
            catch ( TwitterException e )
            {
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void aVoid )
        {
            // This event is handled in TwitterFragment in onReplyDone method
            HashtaggerApp.bus.post( new TwitterReplyEvent( success ) );
        }
    }

    private static class TwitterRetweetTask extends AsyncTask<Void, Void, Void>
    {
        Twitter          twitter;
        long             retweetId;
        int              position;
        twitter4j.Status status;
        boolean success = false;

        public TwitterRetweetTask( long retweetId, int position )
        {
            twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
            twitter.setOAuthAccessToken( new AccessToken( AccountPrefs.getTwitterAccessToken(), AccountPrefs.getTwitterAccessTokenSecret() ) );
            this.retweetId = retweetId;
            this.position = position;
        }

        @Override
        protected Void doInBackground( Void... params )
        {
            try
            {
                status = twitter.retweetStatus( retweetId );
                success = true;
            }
            catch ( TwitterException e )
            {
                Helper.debug( e.getMessage() );
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void aVoid )
        {
            // This event is handled in TwitterFragment in onRetweetDone method
            HashtaggerApp.bus.post( new TwitterRetweetEvent( success, position, status ) );
        }
    }

    private static class TwitterFavoriteTask extends AsyncTask<Void, Void, Void>
    {
        private long             tweetId;
        private int              position;
        private twitter4j.Status status;
        private Twitter          twitter;
        private boolean          isFavorited;
        private boolean success = false;

        public TwitterFavoriteTask( long tweetId, boolean isFavorited, int position )
        {
            this.tweetId = tweetId;
            this.position = position;
            this.isFavorited = isFavorited;
            twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
            twitter.setOAuthAccessToken( new AccessToken( AccountPrefs.getTwitterAccessToken(), AccountPrefs.getTwitterAccessTokenSecret() ) );
        }

        @Override
        protected Void doInBackground( Void... params )
        {
            try
            {
                status = isFavorited ? twitter.destroyFavorite( tweetId ) : twitter.createFavorite( tweetId );
                success = true;
            }
            catch ( TwitterException e )
            {
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void aVoid )
        {
            // This event is handled in TwitterFragment in onFavoritedDone method
            HashtaggerApp.bus.post( new TwitterFavoriteEvent( success, position, status ) );
        }
    }
}
