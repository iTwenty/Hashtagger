package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Intent;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.AuthType;
import net.thetranquilpsychonaut.hashtagger.events.TwitterAuthDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterLoginActivity;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.model.Token;

/**
 * Created by itwenty on 3/15/14.
 */
public class TwitterLoginHandler extends SitesLoginHandler
{
    public interface TwitterLoginListener
    {
        public void whileObtainingReqToken();

        public void onObtainingReqToken( Token requestToken, String authorizationUrl );

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

    public void fetchAccessToken( Token requestToken, String oauthVerifier )
    {
        Intent accessIntent = new Intent( HashtaggerApp.app, TwitterService.class );
        accessIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        accessIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.ACCESS );
        accessIntent.putExtra( TwitterLoginActivity.TWITTER_REQUEST_TOKEN_KEY, requestToken );
        accessIntent.putExtra( TwitterLoginActivity.TWITTER_OAUTH_VERIFIER_KEY, oauthVerifier );
        HashtaggerApp.app.startService( accessIntent );
        twitterLoginListener.whileObtainingAccessToken();
    }

    @Subscribe
    public void onTwitterAuthDone( TwitterAuthDoneEvent event )
    {
        if ( !event.isSuccess() )
        {
            twitterLoginListener.onError();
            return;
        }
        int authType = event.getAuthType();
        switch ( authType )
        {
            case AuthType.REQUEST:
                final Token requestToken = event.getToken();
                final String authorizationUrl = event.getAuthUrl();
                getMainHandler().post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        twitterLoginListener.onObtainingReqToken( requestToken, authorizationUrl );
                    }
                } );
                break;
            case AuthType.ACCESS:
                Token accessToken = event.getToken();
                String userName = event.getUserName();
                AccountPrefs.addTwitterDetails( accessToken.getToken(), accessToken.getSecret(), userName );
                getMainHandler().post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        twitterLoginListener.onUserLoggedIn();
                    }
                } );
        }
    }
}
