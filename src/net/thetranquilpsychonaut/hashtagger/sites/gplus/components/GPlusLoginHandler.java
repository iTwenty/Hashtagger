package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import android.content.Context;
import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.ui.GPlusLoginActivity;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.scribe.model.Token;

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
        accessIntent.putExtra( GPlusLoginActivity.GPLUS_CODE_KEY, code );
        HashtaggerApp.app.startService( accessIntent );
        gPlusLoginListener.whileObtainingAccessToken();
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        int result = intent.getIntExtra( Result.RESULT_KEY, -1 );
        if ( result == Result.FAILURE )
        {
            gPlusLoginListener.onError();
            return;
        }
        Token accessToken = ( Token ) intent.getSerializableExtra( Result.RESULT_DATA );
        String userName = intent.getStringExtra( Result.RESULT_EXTRAS );
        String refreshToken = Helper.extractJsonStringField( accessToken.getRawResponse(), "refresh_token" );
        AccountPrefs.addGPlusDetails( accessToken.getToken(), refreshToken, userName );
        gPlusLoginListener.onUserLoggedIn();
    }

    @Override
    public String getLoginActionName()
    {
        return HashtaggerApp.GPLUS_LOGIN_ACTION;
    }
}
