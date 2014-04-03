package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.AuthType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.AuthActionName;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by itwenty on 3/15/14.
 */
public class TwitterAuthHandler extends BroadcastReceiver implements AuthActionName
{
    public interface TwitterAuthListener
    {
        public void whileObtainingReqToken();

        public void onObtainingReqToken( String authorizationUrl );

        public void whileObtainingAccessToken();

        public void onError();

        public void onUserLoggedIn();
    }

    TwitterAuthListener twitterAuthListener;
    Twitter             twitter;
    RequestToken        requestToken;
    AccessToken         accessToken;
    String              userName;
    Context             context;
    IntentFilter        filter;

    public TwitterAuthHandler( Context context )
    {
        this.context = context;
        twitter = new TwitterFactory( HashtaggerApp.CONFIGURATION ).getInstance();
        filter = new IntentFilter( getAuthActionName() );
        filter.addCategory( Intent.CATEGORY_DEFAULT );
        HashtaggerApp.app.getApplicationContext().registerReceiver( this, filter );
    }

    public void setTwitterAuthListener( TwitterAuthListener twitterAuthListener )
    {
        this.twitterAuthListener = twitterAuthListener;
    }

    public void fetchRequestToken()
    {
        Intent requestIntent = new Intent( HashtaggerApp.app.getApplicationContext(), TwitterService.class );
        requestIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        requestIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.REQUEST );
        requestIntent.putExtra( HashtaggerApp.TWITTER_KEY, twitter );
        HashtaggerApp.app.getApplicationContext().startService( requestIntent );
        twitterAuthListener.whileObtainingReqToken();
    }

    public void fetchAccessToken( String oauthVerifier )
    {
        Intent verifyIntent = new Intent( HashtaggerApp.app.getApplicationContext(), TwitterService.class );
        verifyIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        verifyIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.ACCESS );
        verifyIntent.putExtra( HashtaggerApp.TWITTER_KEY, twitter );
        verifyIntent.putExtra( HashtaggerApp.TWITTER_REQUEST_TOKEN_KEY, requestToken );
        verifyIntent.putExtra( HashtaggerApp.OAUTH_VERIFIER_KEY, oauthVerifier );
        HashtaggerApp.app.getApplicationContext().startService( verifyIntent );
        twitterAuthListener.whileObtainingAccessToken();
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        Result resultType = ( Result ) intent.getSerializableExtra( Result.RESULT_KEY );
        if ( resultType == Result.FAILURE )
        {
            twitterAuthListener.onError();
            return;
        }
        AuthType authType = ( AuthType ) intent.getSerializableExtra( AuthType.AUTH_TYPE_KEY );
        switch ( authType )
        {
            case REQUEST:
                this.requestToken = ( RequestToken ) intent.getSerializableExtra( Result.RESULT_DATA );
                twitterAuthListener.onObtainingReqToken( this.requestToken.getAuthorizationURL() );
                break;
            case ACCESS:
                this.accessToken = ( AccessToken ) intent.getSerializableExtra( Result.RESULT_DATA );
                this.userName = intent.getStringExtra( Result.RESULT_EXTRAS );
                HashtaggerApp.prefs.edit()
                    .putString( HashtaggerApp.TWITTER_OAUTH_ACCESS_TOKEN_KEY, accessToken.getToken() )
                    .putString( HashtaggerApp.TWITTER_OAUTH_ACCESS_TOKEN_SECRET_KEY, accessToken.getTokenSecret() )
                    .putString( HashtaggerApp.USER_KEY, userName )
                    .commit();
                twitterAuthListener.onUserLoggedIn();
        }
    }

    @Override
    public String getAuthActionName()
    {
        return HashtaggerApp.TWITTER_AUTH_ACTION;
    }
}
