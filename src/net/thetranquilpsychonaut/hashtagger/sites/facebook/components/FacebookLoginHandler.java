package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import facebook4j.auth.AccessToken;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.LoginActionName;
import net.thetranquilpsychonaut.hashtagger.utils.SharedPreferencesHelper;

/**
 * Created by itwenty on 4/7/14.
 */
public class FacebookLoginHandler extends BroadcastReceiver implements LoginActionName
{
    public interface FacebookLoginListener
    {
        public void whileObtainingAccessToken();

        public void onError();

        public void onUserLoggedIn();
    }

    IntentFilter          filter;
    FacebookLoginListener facebookLoginListener;

    public FacebookLoginHandler( FacebookLoginListener listener )
    {
        filter = new IntentFilter( getLoginActionName() );
        filter.addCategory( Intent.CATEGORY_DEFAULT );
        HashtaggerApp.app.getApplicationContext().registerReceiver( this, filter );
        facebookLoginListener = listener;
    }

    public void fetchAccessToken( String code )
    {
        Intent accessIntent = new Intent( HashtaggerApp.app.getApplicationContext(), FacebookService.class );
        accessIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        accessIntent.putExtra( HashtaggerApp.FACEBOOK_CODE_KEY, code );
        HashtaggerApp.app.getApplicationContext().startService( accessIntent );
        facebookLoginListener.whileObtainingAccessToken();
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        Result result = ( Result ) intent.getSerializableExtra( Result.RESULT_KEY );
        if ( result == Result.FAILURE )
        {
            facebookLoginListener.onError();
            return;
        }
        AccessToken accessToken = ( AccessToken ) intent.getSerializableExtra( Result.RESULT_DATA );
        String userName = intent.getStringExtra( Result.RESULT_EXTRAS );
        SharedPreferencesHelper.addFacebookDetails( accessToken.getToken(), userName );
        facebookLoginListener.onUserLoggedIn();
    }

    @Override
    public String getLoginActionName()
    {
        return HashtaggerApp.FACEBOOK_LOGIN_ACTION;
    }
}
