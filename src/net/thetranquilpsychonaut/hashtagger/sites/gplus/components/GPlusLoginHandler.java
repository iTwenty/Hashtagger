package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import android.content.Context;
import android.content.Intent;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.SharedPreferencesHelper;

/**
 * Created by itwenty on 5/6/14.
 */
public class GPlusLoginHandler extends SitesLoginHandler
{
    GPlusLoginListener gPlusLoginListener;

    public interface GPlusLoginListener
    {
        public void whileObtainingAccessToken();

        public void onError();

        public void onUserLoggedIn();
    }

    public GPlusLoginHandler( GPlusLoginListener listener )
    {
        this.gPlusLoginListener = listener;
    }

    public void fetchAccessToken( String code )
    {
        Intent accessIntent = new Intent( HashtaggerApp.app, GPlusService.class );
        accessIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        accessIntent.putExtra( HashtaggerApp.GPLUS_CODE_KEY, code );
        HashtaggerApp.app.startService( accessIntent );
        gPlusLoginListener.whileObtainingAccessToken();
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        Helper.debug( "GPlusLoginHandler onReceive" );
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
