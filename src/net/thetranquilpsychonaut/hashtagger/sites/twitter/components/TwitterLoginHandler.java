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
import net.thetranquilpsychonaut.hashtagger.utils.SharedPreferencesHelper;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by itwenty on 3/15/14.
 */
public class TwitterLoginHandler extends BroadcastReceiver implements LoginActionName
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
        Intent requestIntent = new Intent( HashtaggerApp.app.getApplicationContext(), TwitterService.class );
        requestIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        requestIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.REQUEST );
        HashtaggerApp.app.getApplicationContext().startService( requestIntent );
        twitterLoginListener.whileObtainingReqToken();
    }

    public void fetchAccessToken( RequestToken requestToken, String oauthVerifier )
    {
        Intent accessIntent = new Intent( HashtaggerApp.app.getApplicationContext(), TwitterService.class );
        accessIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        accessIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.ACCESS );
        accessIntent.putExtra( HashtaggerApp.TWITTER_REQUEST_TOKEN_KEY, requestToken );
        accessIntent.putExtra( HashtaggerApp.TWITTER_OAUTH_VERIFIER_KEY, oauthVerifier );
        HashtaggerApp.app.getApplicationContext().startService( accessIntent );
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
