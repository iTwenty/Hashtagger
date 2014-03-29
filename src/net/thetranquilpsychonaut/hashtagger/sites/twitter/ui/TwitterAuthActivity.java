package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterAuthHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterAuthHandlerListener;
import twitter4j.auth.RequestToken;

/**
 * Created by itwenty on 3/17/14.
 */
public class TwitterAuthActivity extends FragmentActivity implements TwitterAuthHandlerListener
{
    WebView            wvTwitterSignIn;
    TwitterAuthHandler authHandler;
    ProgressBar        pgbrLoadingAuth;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_twitter_auth );
        wvTwitterSignIn = ( WebView ) findViewById( R.id.wv_twitter_sign_in );
        pgbrLoadingAuth = ( ProgressBar ) findViewById( R.id.pgbr_loading_auth );
        setTitle( getString( R.string.str_title_activity_twitter_auth ) );
        authHandler = new TwitterAuthHandler( this );
        authHandler.setListener( this );
        authHandler.authorizeUser();
    }

    @Override
    protected void onNewIntent( Intent intent )
    {
        setIntent( intent );
        handleIntent( intent );
    }

    private void handleIntent( Intent intent )
    {
        Uri uri = intent.getData();
        Helper.debug( uri.toString() );
        if ( null != uri && uri.toString().startsWith( HashtaggerApp.CALLBACK_URL ) && null != uri.getQueryParameter( HashtaggerApp.OAUTH_VERIFIER_KEY ) )
        {
            authHandler.verifyUser( uri.getQueryParameter( HashtaggerApp.OAUTH_VERIFIER_KEY ) );
        }
        else
        {
            setResult( RESULT_CANCELED );
            finish();
        }
    }

    @Override
    public void onError()
    {
        setResult( RESULT_CANCELED );
        finish();
    }

    @Override
    public void whileObtainingReqToken()
    {
        wvTwitterSignIn.setVisibility( View.GONE );
        pgbrLoadingAuth.setVisibility( View.VISIBLE );
    }

    @Override
    public void onObtainingReqToken( RequestToken requestToken )
    {
        pgbrLoadingAuth.setVisibility( View.GONE );
        wvTwitterSignIn.setVisibility( View.VISIBLE );
        wvTwitterSignIn.loadUrl( requestToken.getAuthorizationURL() );
        Helper.debug( requestToken.getAuthorizationURL() );
    }

    @Override
    public void onUserLoggedIn()
    {
        setResult( RESULT_OK );
        finish();
    }
}
