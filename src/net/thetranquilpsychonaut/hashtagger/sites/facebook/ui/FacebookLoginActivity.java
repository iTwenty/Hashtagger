package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import facebook4j.FacebookFactory;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
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
                if ( !url.startsWith( HashtaggerApp.FACEBOOK_CALLBACK_URL ) )
                {
                    return false;
                }
                Uri uri = Uri.parse( url );
                if ( null != uri.getQueryParameter( HashtaggerApp.FACEBOOK_CODE_KEY ) )
                {
                    ( ( FacebookLoginHandler ) sitesLoginHandler ).fetchAccessToken( uri.getQueryParameter( HashtaggerApp.FACEBOOK_CODE_KEY ) );
                }
                else
                {
                    setResult( RESULT_CANCELED );
                    finish();
                }
                return true;
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
            wvFacebookLogin.loadUrl( new FacebookFactory( FacebookConfig.CONFIGURATION ).getInstance()
                    .getOAuthAuthorizationURL( HashtaggerApp.FACEBOOK_CALLBACK_URL ) );
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
