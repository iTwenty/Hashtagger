package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Context;
import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.AuthType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by itwenty on 3/15/14.
 */
public class TwitterLoginHandler extends SitesLoginHandler
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

    public TwitterLoginHandler( TwitterLoginListener listener )
    {
        twitterLoginListener = listener;
    }

    public void fetchRequestToken()
    {
        Intent requestIntent = new Intent( HashtaggerApp.app, TwitterService.class );
        requestIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        requestIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.REQUEST );
        HashtaggerApp.app.startService( requestIntent );
        twitterLoginListener.whileObtainingReqToken();
    }

    public void fetchAccessToken( RequestToken requestToken, String oauthVerifier )
    {
        Intent accessIntent = new Intent( HashtaggerApp.app, TwitterService.class );
        accessIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        accessIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.ACCESS );
        accessIntent.putExtra( HashtaggerApp.TWITTER_REQUEST_TOKEN_KEY, requestToken );
        accessIntent.putExtra( HashtaggerApp.TWITTER_OAUTH_VERIFIER_KEY, oauthVerifier );
        HashtaggerApp.app.startService( accessIntent );
        twitterLoginListener.whileObtainingAccessToken();
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        int resultType = intent.getIntExtra( Result.RESULT_KEY, -1 );
        if ( resultType == Result.FAILURE )
        {
            twitterLoginListener.onError();
            return;
        }
        int authType = intent.getIntExtra( AuthType.AUTH_TYPE_KEY, -1 );
        switch ( authType )
        {
            case AuthType.REQUEST:
                RequestToken requestToken = ( RequestToken ) intent.getSerializableExtra( Result.RESULT_DATA );
                twitterLoginListener.onObtainingReqToken( requestToken );
                break;
            case AuthType.ACCESS:
                AccessToken accessToken = ( AccessToken ) intent.getSerializableExtra( Result.RESULT_DATA );
                String userName = intent.getStringExtra( Result.RESULT_EXTRAS );
                AccountPrefs.addTwitterDetails( accessToken.getToken(), accessToken.getTokenSecret(), userName );
                twitterLoginListener.onUserLoggedIn();
        }
    }

    @Override
    public String getLoginActionName()
    {
        return HashtaggerApp.TWITTER_LOGIN_ACTION;
    }
}
