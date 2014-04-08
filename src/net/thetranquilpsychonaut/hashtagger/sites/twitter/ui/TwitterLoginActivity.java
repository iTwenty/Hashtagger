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
 * <p/>
 * The login flow for twitter involves using the consumerKey and consumerSecret to fetch a request token.
 * Once we have the request token, we use the authorization url within the token to show a login+confirm access page
 * to the user. After getting user auth, we let the webpage redirect to our login activity with an oauth code
 * which, alongwith the request token, is used to fetch the access token.
 */
public class TwitterLoginActivity extends LoadingActivity implements TwitterLoginHandler.TwitterLoginListener
{
    WebView             wvTwitterLogin;
    TwitterLoginHandler twitterLoginHandler;
    // We need to persist this request token throughout the entire login procedure. So make sure it
    // does not get destroyed anytime during activity lifecycle!
    RequestToken        requestToken;

    @Override
    protected View initMainView( Bundle savedInstanceState )
    {
        wvTwitterLogin = new WebView( this );
        return wvTwitterLogin;
    }


    @Override
    protected void onViewsCreated( Bundle savedInstanceState )
    {
        setTitle( getString( R.string.str_title_activity_twitter_login ) );
        twitterLoginHandler = new TwitterLoginHandler( this );
        // On first start we need to fetch the request token. On subsequent starts, we need to
        // ensure that fetched request token is not lost.
        if ( null != savedInstanceState )
        {
            wvTwitterLogin.restoreState( savedInstanceState );
            requestToken = ( RequestToken ) savedInstanceState.getSerializable( HashtaggerApp.TWITTER_REQUEST_TOKEN_KEY );
        }
        else
        {
            twitterLoginHandler.fetchRequestToken();
        }
    }

    // This method is invoked by the callback URL after user has logged in and authorized the app
    // The scheme of the intent is unique enough to not trigger the Android activity selection dialog
    // and instead deliver the intent directly to this activity
    @Override
    protected void onNewIntent( Intent intent )
    {
        setIntent( intent );
        handleIntent( intent );
        setIntent( null );
    }

    // Use the request token and the code in the callback delivered intent to fetch access token
    private void handleIntent( Intent intent )
    {
        Uri uri = intent.getData();
        if ( null != uri && uri.toString().startsWith( HashtaggerApp.TWITTER_CALLBACK_URL ) && null != uri.getQueryParameter( HashtaggerApp.TWITTER_OAUTH_VERIFIER_KEY ) )
        {
            twitterLoginHandler.fetchAccessToken( this.requestToken, uri.getQueryParameter( HashtaggerApp.TWITTER_OAUTH_VERIFIER_KEY ) );
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
        wvTwitterLogin.saveState( outState );
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
        wvTwitterLogin.loadUrl( this.requestToken.getAuthorizationURL() );
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
