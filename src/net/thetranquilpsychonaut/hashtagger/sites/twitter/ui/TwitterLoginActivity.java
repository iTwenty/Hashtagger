package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.LoadingActivity;
import twitter4j.auth.RequestToken;

/**
 * Created by itwenty on 3/17/14.
 */
public class TwitterLoginActivity extends LoadingActivity implements TwitterLoginHandler.TwitterLoginListener
{
    WebView             wvTwitterSignIn;
    TwitterLoginHandler loginHandler;
    // We need to persist this request token throughout the entire login procedure. So make sure it
    // does not get destroyed anytime during activity lifecycle!
    RequestToken        requestToken;

    @Override
    protected View initMainView( Bundle savedInstanceState )
    {
        wvTwitterSignIn = new WebView( this );
        return wvTwitterSignIn;
    }


    @Override
    protected void onViewsCreated( Bundle savedInstanceState )
    {
        setTitle( getString( R.string.str_title_activity_twitter_auth ) );
        loginHandler = new TwitterLoginHandler( this );
        if ( null != savedInstanceState )
        {
            wvTwitterSignIn.restoreState( savedInstanceState );
            requestToken = ( RequestToken ) savedInstanceState.getSerializable( HashtaggerApp.TWITTER_REQUEST_TOKEN_KEY );
        }
        else
        {
            loginHandler.fetchRequestToken();
        }
    }

    @Override
    protected void onNewIntent( Intent intent )
    {
        setIntent( intent );
        handleIntent( intent );
        setIntent( null );
    }

    private void handleIntent( Intent intent )
    {
        Uri uri = intent.getData();
        if ( null != uri && uri.toString().startsWith( HashtaggerApp.CALLBACK_URL ) && null != uri.getQueryParameter( HashtaggerApp.OAUTH_VERIFIER_KEY ) )
        {
            loginHandler.fetchAccessToken( this.requestToken, uri.getQueryParameter( HashtaggerApp.OAUTH_VERIFIER_KEY ) );
        }
        else
        {
            setResult( RESULT_CANCELED );
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        wvTwitterSignIn.saveState( outState );
        outState.putSerializable( HashtaggerApp.TWITTER_REQUEST_TOKEN_KEY, requestToken );
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
        showLoadingView();
    }

    @Override
    public void onObtainingReqToken( RequestToken requestToken )
    {
        showMainView();
        this.requestToken = requestToken;
        wvTwitterSignIn.loadUrl( this.requestToken.getAuthorizationURL() );
    }

    @Override
    public void whileObtainingAccessToken()
    {
        showLoadingView();
    }

    @Override
    public void onUserLoggedIn()
    {
        setResult( RESULT_OK );
        finish();
    }
}
