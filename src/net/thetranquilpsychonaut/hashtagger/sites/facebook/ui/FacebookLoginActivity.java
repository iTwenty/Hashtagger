package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.config.FacebookConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.components.FacebookLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesLoginActivity;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookLoginActivity extends SitesLoginActivity implements FacebookLoginHandler.FacebookLoginListener
{
    public static final String FACEBOOK_CALLBACK_URL  = "http://localhost/";
    public static final String FACEBOOK_CODE_KEY      = "code";
    public static final String FACEBOOK_AUTHORIZE_URL = String.format( "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s", FacebookConfig.FACEBOOK_OAUTH_APP_ID, FACEBOOK_CALLBACK_URL );

    WebView wvFacebookLogin;

    @Override
    protected View initMainView( Bundle savedInstanceState )
    {
        wvFacebookLogin = new WebView( this );
        wvFacebookLogin.setWebViewClient( new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading( WebView view, String url )
            {
                if ( !url.startsWith( FACEBOOK_CALLBACK_URL ) )
                {
                    return false;
                }
                Uri uri = Uri.parse( url );
                if ( null != uri.getQueryParameter( FACEBOOK_CODE_KEY ) )
                {
                    String code = uri.getQueryParameter( FACEBOOK_CODE_KEY );
                    ( ( FacebookLoginHandler ) sitesLoginHandler ).fetchAccessToken( code );
                }
                else
                {
                    setResult( RESULT_CANCELED );
                    finish();
                }
                return true;
            }

            @Override
            public void onPageStarted( WebView view, String url, Bitmap favicon )
            {
                super.onPageStarted( view, url, favicon );
                showLoadingView();
            }

            @Override
            public void onPageFinished( WebView view, String url )
            {
                super.onPageFinished( view, url );
                showMainView();
            }
        } );
        return wvFacebookLogin;
    }

    @Override
    protected void onViewsCreated( Bundle savedInstanceState )
    {
        setTitle( getString( R.string.str_title_activity_facebook_login ) );
        if ( null != savedInstanceState )
        {
            wvFacebookLogin.restoreState( savedInstanceState );
        }
        else
        {
            wvFacebookLogin.loadUrl( FACEBOOK_AUTHORIZE_URL );
        }
    }

    @Override
    protected SitesLoginHandler initSitesLoginHandler()
    {
        return new FacebookLoginHandler( this );
    }


    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        wvFacebookLogin.saveState( outState );
    }

    @Override
    public void whileObtainingAccessToken()
    {
        showLoadingView();
    }

    @Override
    public void onError()
    {
        setResult( RESULT_CANCELED );
        finish();
    }

    @Override
    public void onUserLoggedIn()
    {
        setResult( RESULT_OK );
        finish();
    }
}
