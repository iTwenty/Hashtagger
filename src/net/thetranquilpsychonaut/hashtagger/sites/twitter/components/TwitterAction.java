package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.os.AsyncTask;
import net.thetranquilpsychonaut.hashtagger.config.TwitterConfig;
import net.thetranquilpsychonaut.hashtagger.utils.SharedPreferencesHelper;
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
    public static interface TwitterActionListener
    {
        void onPerforming();

        void onPerformed();

        void onError();
    }

    private TwitterActionListener listener;
    private TwitterReplyTask      twitterReplyTask;
    private TwitterRetweetTask    twitterRetweetTask;

    public TwitterAction( TwitterActionListener listener )
    {
        this.listener = listener;
    }

    public void executeReplyAction( String reply, long inReplyToUserId )
    {
        new TwitterReplyTask( reply, inReplyToUserId ).execute();
    }

    public void executeRetweetAction( long retweetId )
    {
        new TwitterRetweetTask( retweetId ).execute();
    }

    private class TwitterReplyTask extends AsyncTask<Void, Void, Void>
    {
        Twitter twitter;
        String  reply;
        long    inReplyToUserId;
        boolean success = false;

        public TwitterReplyTask( String reply, long inReplyToUserId )
        {
            twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
            twitter.setOAuthAccessToken( new AccessToken( SharedPreferencesHelper.getTwitterAccessToken(), SharedPreferencesHelper.getTwitterAccessTokenSecret() ) );
            this.reply = reply;
            this.inReplyToUserId = inReplyToUserId;
        }

        @Override
        protected void onPreExecute()
        {
            if ( null != listener )
            {
                listener.onPerforming();
            }
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
            if ( null != listener )
            {
                if ( !success )
                {
                    listener.onError();
                }
                else
                {
                    listener.onPerformed();
                }
            }
        }
    }

    private class TwitterRetweetTask extends AsyncTask<Void, Void, Void>
    {
        Twitter twitter;
        long    retweetId;
        boolean success = false;

        public TwitterRetweetTask( long retweetId )
        {
            twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
            twitter.setOAuthAccessToken( new AccessToken( SharedPreferencesHelper.getTwitterAccessToken(), SharedPreferencesHelper.getTwitterAccessTokenSecret() ) );
            this.retweetId = retweetId;
        }

        @Override
        protected void onPreExecute()
        {
            if ( null != listener )
            {
                listener.onPerforming();
            }
        }

        @Override
        protected Void doInBackground( Void... params )
        {
            try
            {
                twitter.retweetStatus( retweetId );
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
            if ( null != listener )
            {
                if ( !success )
                {
                    listener.onError();
                }
                else
                {
                    listener.onPerformed();
                }
            }
        }
    }
}
