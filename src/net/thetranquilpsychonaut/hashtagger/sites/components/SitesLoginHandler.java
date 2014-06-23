package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.ActionType;
import net.thetranquilpsychonaut.hashtagger.enums.AuthType;
import org.scribe.model.Token;

/**
 * Created by itwenty on 5/16/14.
 */
public abstract class SitesLoginHandler
{
    public static final String REQUEST_TOKEN_KEY = "request_token";
    public static final String VERIFIER_KEY      = "verifier";

    public interface SitesLoginListener
    {
        public void whileObtainingReqToken();

        public void onObtainingReqToken( Token requestToken, String authorizationUrl );

        public void whileObtainingAccessToken();

        public void onError();

        public void onUserLoggedIn();
    }

    protected SitesLoginListener listener;

    public SitesLoginHandler( SitesLoginListener listener )
    {
        this.listener = listener;
    }

    private Handler mainHandler = new Handler( Looper.getMainLooper() );

    protected Handler getMainHandler()
    {
        return this.mainHandler;
    }

    public void fetchRequestToken()
    {
        Intent requestIntent = new Intent( HashtaggerApp.app, getServiceClass() );
        requestIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        requestIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.REQUEST );
        HashtaggerApp.app.startService( requestIntent );
        listener.whileObtainingReqToken();
    }

    public void fetchAccessToken( Token requestToken, String oauthVerifier )
    {
        Intent accessIntent = new Intent( HashtaggerApp.app, getServiceClass() );
        accessIntent.putExtra( ActionType.ACTION_TYPE_KEY, ActionType.AUTH );
        accessIntent.putExtra( AuthType.AUTH_TYPE_KEY, AuthType.ACCESS );
        accessIntent.putExtra( REQUEST_TOKEN_KEY, requestToken );
        accessIntent.putExtra( VERIFIER_KEY, oauthVerifier );
        HashtaggerApp.app.startService( accessIntent );
        listener.whileObtainingAccessToken();
    }

    protected abstract Class<?> getServiceClass();
}
