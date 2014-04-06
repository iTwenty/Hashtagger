package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.AuthType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.LoginActionName;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.Serializable;

/**
 * Created by itwenty on 3/15/14.
 */
public class TwitterLoginHandler extends BroadcastReceiver implements LoginActionName, Serializable
{
    public interface TwitterLoginListener
    {
        public void whileObtainingReqToken();

        public void onObtainingReqToken( RequestToken requestToken );

        public void whileObtainingAccessToken();

        public void onError();

        public void onUserLoggedIn();
    }

    TwitterLoginListener twitterLoginListener;
    IntentFilter         filter;

    public TwitterLoginHandler( TwitterLoginListener listener )
    {
        filter = new IntentFilter( getLoginActionName() );
        filter.addCategory( Intent.CATEGORY_DEFAULT );
        HashtaggerApp.app.getApplicationContext().registerReceiver( this, filter );
        twitterLoginListener = listener;
    }

    public void fetchRequestToken()
    {
        Twitter twitter = new TwitterFactory( HashtaggerApp.CONFIGURATION ).getInstance();
        Intent requestIntent = new Intent( HashtaggerApp.app.getApplicationContext(), TwitterService.class );
        requestIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        requestIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.REQUEST );
        requestIntent.putExtra( HashtaggerApp.TWITTER_KEY, twitter );
        HashtaggerApp.app.getApplicationContext().startService( requestIntent );
        twitterLoginListener.whileObtainingReqToken();
    }

    public void fetchAccessToken( RequestToken requestToken, String oauthVerifier )
    {
        Twitter twitter = new TwitterFactory( HashtaggerApp.CONFIGURATION ).getInstance();
        Intent verifyIntent = new Intent( HashtaggerApp.app.getApplicationContext(), TwitterService.class );
        verifyIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        verifyIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.ACCESS );
        verifyIntent.putExtra( HashtaggerApp.TWITTER_KEY, twitter );
        verifyIntent.putExtra( HashtaggerApp.TWITTER_REQUEST_TOKEN_KEY, requestToken );
        verifyIntent.putExtra( HashtaggerApp.OAUTH_VERIFIER_KEY, oauthVerifier );
        HashtaggerApp.app.getApplicationContext().startService( verifyIntent );
        twitterLoginListener.whileObtainingAccessToken();
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        Result resultType = ( Result ) intent.getSerializableExtra( Result.RESULT_KEY );
        if ( resultType == Result.FAILURE )
        {
            twitterLoginListener.onError();
            return;
        }
        AuthType authType = ( AuthType ) intent.getSerializableExtra( AuthType.AUTH_TYPE_KEY );
        switch ( authType )
        {
            case REQUEST:
                RequestToken requestToken = ( RequestToken ) intent.getSerializableExtra( Result.RESULT_DATA );
                twitterLoginListener.onObtainingReqToken( requestToken );
                break;
            case ACCESS:
                AccessToken accessToken = ( AccessToken ) intent.getSerializableExtra( Result.RESULT_DATA );
                String userName = intent.getStringExtra( Result.RESULT_EXTRAS );
                SharedPreferencesHelper.addTwitterDetails( accessToken.getToken(), accessToken.getTokenSecret(), userName );
                twitterLoginListener.onUserLoggedIn();
        }
    }

    @Override
    public String getLoginActionName()
    {
        return HashtaggerApp.TWITTER_LOGIN_ACTION;
    }
}
