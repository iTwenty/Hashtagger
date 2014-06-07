package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesLoginActivity;
import org.scribe.model.Token;

/**
 * Created by itwenty on 3/17/14.
 * <p/>
 * The login flow for twitter involves using the consumerKey and consumerSecret to fetch a request token.
 * Once we have the request token, we use the authorization url within the token to show a login+confirm access page
 * to the user. After getting user auth, we let the webpage redirect to our login activity with an oauth code
 * which, alongwith the request token, is used to fetch the access token.
 */
public class TwitterLoginActivity extends SitesLoginActivity implements TwitterLoginHandler.TwitterLoginListener
{
    public static final String TWITTER_REQUEST_TOKEN_KEY  = "twitter_request_token_key";
    public static final String TWITTER_CALLBACK_URL       = "twitter-login-callback:///";
    public static final String TWITTER_OAUTH_VERIFIER_KEY = "oauth_verifier";

    WebView wvTwitterLogin;
    Token   requestToken;

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
        if ( null != savedInstanceState )
        {
            wvTwitterLogin.restoreState( savedInstanceState );
            requestToken = ( Token ) savedInstanceState.getSerializable( TWITTER_REQUEST_TOKEN_KEY );
        }
        else
        {
            ( ( TwitterLoginHandler ) sitesLoginHandler ).fetchRequestToken();
        }
    }

    @Override
    protected SitesLoginHandler initSitesLoginHandler()
    {
        return new TwitterLoginHandler( this );
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
        if ( null != uri && uri.toString().startsWith( TWITTER_CALLBACK_URL ) && null != uri.getQueryParameter( TWITTER_OAUTH_VERIFIER_KEY ) )
        {
            String oauthVerifier = uri.getQueryParameter( TWITTER_OAUTH_VERIFIER_KEY );
            ( ( TwitterLoginHandler ) sitesLoginHandler ).fetchAccessToken( this.requestToken, oauthVerifier );
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
        outState.putSerializable( TWITTER_REQUEST_TOKEN_KEY, requestToken );
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
    public void onObtainingReqToken( Token requestToken, String authorizationUrl )
    {
        showMainView();
        this.requestToken = requestToken;
        wvTwitterLogin.loadUrl( authorizationUrl );
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
