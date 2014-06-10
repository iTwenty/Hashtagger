package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.config.GPlusConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesLoginActivity;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusLoginActivity extends SitesLoginActivity implements GPlusLoginHandler.GPlusLoginListener
{
    public static final  String GPLUS_CALLBACK_URL  = "http://localhost/";
    public static final  String GPLUS_CODE_KEY      = "code";
    private static final String GPLUS_AUTHORIZE_URL =
            String.format( "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s&scope=%s",
                    GPlusConfig.GPLUS_OAUTH_CLIENT_ID, GPLUS_CALLBACK_URL, GPlusConfig.GPLUS_ACCESS_SCOPE );

    WebView wvGPlusLogin;

    @Override
    protected View initMainView( Bundle savedInstanceState )
    {
        wvGPlusLogin = new WebView( this );
        wvGPlusLogin.getSettings().setJavaScriptEnabled( true );
        wvGPlusLogin.setWebViewClient( new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading( WebView view, String url )
            {
                if ( !url.startsWith( GPLUS_CALLBACK_URL ) )
                {
                    return false;
                }
                Uri uri = Uri.parse( url );
                if ( null != uri.getQueryParameter( GPLUS_CODE_KEY ) )
                {
                    String code = uri.getQueryParameter( GPLUS_CODE_KEY );
                    ( ( GPlusLoginHandler ) sitesLoginHandler ).fetchAccessToken( code );
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
        return wvGPlusLogin;
    }

    @Override
    protected void onViewsCreated( Bundle savedInstanceState )
    {
        setTitle( getString( R.string.str_title_login_activity_gplus ) );
        if ( null != savedInstanceState )
        {
            wvGPlusLogin.restoreState( savedInstanceState );
        }
        else
        {
            wvGPlusLogin.loadUrl( GPLUS_AUTHORIZE_URL );
        }
    }

    @Override
    protected SitesLoginHandler initSitesLoginHandler()
    {
        return new GPlusLoginHandler( this );
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        wvGPlusLogin.saveState( outState );
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
