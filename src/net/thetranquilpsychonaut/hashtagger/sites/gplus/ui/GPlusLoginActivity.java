package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.config.GPlusConfig;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.LoadingActivity;

import java.util.Arrays;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusLoginActivity extends LoadingActivity implements GPlusLoginHandler.GPlusLoginListener
{
    WebView           wvGPlusLogin;
    GPlusLoginHandler gPlusLoginHandler;

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
                Helper.debug( url );
                if ( !url.startsWith( HashtaggerApp.GPLUS_CALLBACK_URL ) )
                {
                    wvGPlusLogin.loadUrl( url );
                    return false;
                }
                Uri uri = Uri.parse( url );
                if ( null != uri.getQueryParameter( HashtaggerApp.GPLUS_CODE_KEY ) )
                {
                    gPlusLoginHandler.fetchAccessToken( uri.getQueryParameter( HashtaggerApp.GPLUS_CODE_KEY ) );
                }
                else
                {
                    setResult( RESULT_CANCELED );
                    finish();
                }
                return true;
            }
        } );
        return wvGPlusLogin;
    }

    @Override
    protected void onViewsCreated( Bundle savedInstanceState )
    {
        setTitle( getString( R.string.str_title_login_activity_gplus ) );
        gPlusLoginHandler = new GPlusLoginHandler( this );
        if ( null != savedInstanceState )
        {
            wvGPlusLogin.restoreState( savedInstanceState );
        }
        else
        {
            String authUrl = new GoogleAuthorizationCodeRequestUrl(
                    GPlusConfig.SECRETS,
                    HashtaggerApp.GPLUS_CALLBACK_URL,
                    Arrays.asList( HashtaggerApp.GPLUS_ACCESS_SCOPE ) ).build();
            Helper.debug( authUrl );
            wvGPlusLogin.loadUrl( authUrl );
        }
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
