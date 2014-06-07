package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Context;
import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.ui.FacebookLoginActivity;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.model.Token;

/**
 * Created by itwenty on 4/7/14.
 */
public class FacebookLoginHandler extends SitesLoginHandler
{
    public interface FacebookLoginListener
    {
        public void whileObtainingAccessToken();

        public void onError();

        public void onUserLoggedIn();
    }

    FacebookLoginListener facebookLoginListener;

    public FacebookLoginHandler( FacebookLoginListener listener )
    {
        facebookLoginListener = listener;
    }

    public void fetchAccessToken( String code )
    {
        Intent accessIntent = new Intent( HashtaggerApp.app, FacebookService.class );
        accessIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        accessIntent.putExtra( FacebookLoginActivity.FACEBOOK_CODE_KEY, code );
        HashtaggerApp.app.startService( accessIntent );
        facebookLoginListener.whileObtainingAccessToken();
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        int result = intent.getIntExtra( Result.RESULT_KEY, -1 );
        if ( result == Result.FAILURE )
        {
            facebookLoginListener.onError();
            return;
        }
        Token accessToken = ( Token ) intent.getSerializableExtra( Result.RESULT_DATA );
        String userName = intent.getStringExtra( Result.RESULT_EXTRAS );
        AccountPrefs.addFacebookDetails( accessToken.getToken(), userName );
        facebookLoginListener.onUserLoggedIn();
    }

    @Override
    public String getLoginActionName()
    {
        return HashtaggerApp.FACEBOOK_LOGIN_ACTION;
    }
}
