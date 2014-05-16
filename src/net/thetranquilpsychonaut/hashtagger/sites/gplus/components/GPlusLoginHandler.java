package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.LoginActionName;
import net.thetranquilpsychonaut.hashtagger.utils.SharedPreferencesHelper;

/**
 * Created by itwenty on 5/6/14.
 */
public class GPlusLoginHandler extends BroadcastReceiver implements LoginActionName
{
    IntentFilter       filter;
    GPlusLoginListener gPlusLoginListener;

    public interface GPlusLoginListener
    {
        public void whileObtainingAccessToken();

        public void onError();

        public void onUserLoggedIn();
    }

    public GPlusLoginHandler( GPlusLoginListener listener )
    {
        filter = new IntentFilter( getLoginActionName() );
        filter.addCategory( Intent.CATEGORY_DEFAULT );
        HashtaggerApp.app.getApplicationContext().registerReceiver( this, filter );
        this.gPlusLoginListener = listener;
    }

    public void fetchAccessToken( String code )
    {
        Intent accessIntent = new Intent( HashtaggerApp.app.getApplicationContext(), GPlusService.class );
        accessIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        accessIntent.putExtra( HashtaggerApp.GPLUS_CODE_KEY, code );
        HashtaggerApp.app.getApplicationContext().startService( accessIntent );
        gPlusLoginListener.whileObtainingAccessToken();
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        Result result = ( Result ) intent.getSerializableExtra( Result.RESULT_KEY );
        if ( result == Result.FAILURE )
        {
            gPlusLoginListener.onError();
            return;
        }
        GoogleTokenResponse tokenResponse = GPlusData.AuthData.popTokenResponse();
        String userName = intent.getStringExtra( Result.RESULT_EXTRAS );
        SharedPreferencesHelper.addGPlusDetails( tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), userName );
        gPlusLoginListener.onUserLoggedIn();
    }

    @Override
    public String getLoginActionName()
    {
        return HashtaggerApp.GPLUS_LOGIN_ACTION;
    }
}
