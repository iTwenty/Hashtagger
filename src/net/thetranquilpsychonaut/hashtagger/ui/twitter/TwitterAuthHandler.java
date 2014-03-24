package net.thetranquilpsychonaut.hashtagger.ui.twitter;

import android.content.Context;
import android.os.AsyncTask;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.ui.SitesAuthHandler;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by itwenty on 3/15/14.
 */
public class TwitterAuthHandler implements SitesAuthHandler
{
    TwitterAuthHandlerListener listener;
    TwitterAuthTask            twitterAuthTask;
    TwitterVerifyTask          twitterVerifyTask;
    Twitter                    twitter;
    RequestToken               requestToken;
    AccessToken                accessToken;
    String                     userName;
    Context                    context;

    public TwitterAuthHandler( Context context )
    {
        this.context = context;
        twitterAuthTask = new TwitterAuthTask( this );
        twitterVerifyTask = new TwitterVerifyTask( this );
        twitter = new TwitterFactory( HashtaggerApp.CONFIGURATION ).getInstance();
    }

    public void setListener( TwitterAuthHandlerListener tahl )
    {
        this.listener = tahl;
    }

    public void authorizeUser()
    {
        twitterAuthTask.execute();
    }

    public void verifyUser( String oauthVerifier )
    {
        twitterVerifyTask.execute( oauthVerifier );
    }

    private static class TwitterAuthTask extends AsyncTask<Void, Void, Void>
    {
        TwitterAuthHandler authHandler;

        public TwitterAuthTask( TwitterAuthHandler tah )
        {
            this.authHandler = tah;
        }

        @Override
        protected void onPreExecute()
        {
            authHandler.listener.whileObtainingReqToken();
        }

        @Override
        protected Void doInBackground( Void... params )
        {
            try
            {
                authHandler.requestToken = authHandler.twitter.getOAuthRequestToken( HashtaggerApp.CALLBACK_URL );
            }
            catch ( Exception e )
            {
                Helper.debug( "Error while obtaining request token" );
                authHandler.listener.onError();
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void aVoid )
        {
            if ( null != authHandler.requestToken )
            {
                authHandler.listener.onObtainingReqToken( authHandler.requestToken );
            }
            else
            {
                Helper.debug( "Request token is null" );
                authHandler.listener.onError();
            }
        }
    }

    private static class TwitterVerifyTask extends AsyncTask<String, Void, Void>
    {
        TwitterAuthHandler authHandler;

        public TwitterVerifyTask( TwitterAuthHandler tah )
        {
            this.authHandler = tah;
        }

        @Override
        protected void onPreExecute()
        {
            authHandler.listener.whileObtainingReqToken();
        }

        @Override
        protected Void doInBackground( String... params )
        {
            try
            {
                authHandler.accessToken = authHandler.twitter.getOAuthAccessToken( authHandler.requestToken, params[0] );
                authHandler.twitter.setOAuthAccessToken( authHandler.accessToken );
                authHandler.userName = authHandler.twitter.verifyCredentials().getName();
            }
            catch ( TwitterException e )
            {
                Helper.debug( "Error while obtaining access token" );
                authHandler.listener.onError();
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void aVoid )
        {
            if( null != authHandler.accessToken )
            {
                HashtaggerApp.prefs.edit()
                    .putString( HashtaggerApp.TWITTER_OAUTH_ACCESS_TOKEN_KEY, authHandler.accessToken.getToken() )
                    .putString( HashtaggerApp.TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY, authHandler.accessToken.getTokenSecret() )
                    .putString( HashtaggerApp.USER_KEY, authHandler.userName )
                    .commit();
                authHandler.listener.onUserLoggedIn();
            }
            else
            {
                Helper.debug( "Access token is null" );
                authHandler.listener.onError();
            }
        }
    }
}
