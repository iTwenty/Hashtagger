package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ViewAnimator;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import org.scribe.model.Token;

/**
 * Created by itwenty on 4/5/14.
 */
public abstract class SitesLoginActivity extends FragmentActivity implements SitesLoginHandler.SitesLoginListener
{
    private static final String ACTIVE_VIEW_KEY   = "active_view_key";
    private static final String REQUEST_TOKEN_KEY = "request_token";

    private static final int MAIN_VIEW    = 0;
    private static final int LOADING_VIEW = 1;

    private ViewAnimator vaLoadingView;
    private WebView      webView;
    private View         loadingView;
    private Token        requestToken;

    protected SitesLoginHandler sitesLoginHandler;

    private int activeView = MAIN_VIEW;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_loading );
        sitesLoginHandler = initSitesLoginHandler();
        vaLoadingView = ( ViewAnimator ) findViewById( R.id.va_loading_view );
        webView = initMainView( savedInstanceState );
        loadingView = initLoadingView( savedInstanceState );
        vaLoadingView.addView( webView, MAIN_VIEW );
        vaLoadingView.addView( loadingView, LOADING_VIEW );
        if ( null != savedInstanceState )
        {
            activeView = savedInstanceState.getInt( ACTIVE_VIEW_KEY );
            showView( activeView );
            if ( savedInstanceState.containsKey( REQUEST_TOKEN_KEY ) )
            {
                this.requestToken = ( Token ) savedInstanceState.getSerializable( REQUEST_TOKEN_KEY );
            }
            webView.restoreState( savedInstanceState );
        }
        else
        {
            switch ( getOAuthVersion() )
            {
                case 1: sitesLoginHandler.fetchRequestToken(); break;
                case 2: webView.loadUrl( getAuthorizationUrl() );
            }
        }
    }

    protected abstract int getOAuthVersion();

    protected abstract String getAuthorizationUrl();

    @Override
    protected void onStart()
    {
        super.onStart();
        HashtaggerApp.bus.register( sitesLoginHandler );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        HashtaggerApp.bus.unregister( sitesLoginHandler );
    }

    protected abstract SitesLoginHandler initSitesLoginHandler();

    protected WebView initMainView( Bundle savedInstanceState )
    {
        webView = new WebView( this );
        webView.getSettings().setJavaScriptEnabled( true );
        webView.setWebViewClient( new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading( WebView view, String url )
            {
                if ( !url.startsWith( getCallbackUrl() ) )
                {
                    return false;
                }
                Uri uri = Uri.parse( url );
                if ( null != uri.getQueryParameter( getVerifierKey() ) )
                {
                    String code = uri.getQueryParameter( getVerifierKey() );
                    sitesLoginHandler.fetchAccessToken( requestToken, code );
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
        return webView;
    }

    private View initLoadingView( Bundle savedInstanceState )
    {
        return getLayoutInflater().inflate( R.layout.sites_view_loading, null );
    }

    protected abstract String getVerifierKey();

    protected abstract String getCallbackUrl();

    public void showView( int viewIndex )
    {
        switch ( viewIndex )
        {
            case LOADING_VIEW:
                showLoadingView();
                break;
            case MAIN_VIEW:
                showMainView();
                break;
            default:
                throw new RuntimeException( "View not in activity" );
        }
    }

    public void showLoadingView()
    {
        vaLoadingView.setDisplayedChild( LOADING_VIEW );
        activeView = LOADING_VIEW;
    }

    public void showMainView()
    {
        vaLoadingView.setDisplayedChild( MAIN_VIEW );
        activeView = MAIN_VIEW;
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putInt( ACTIVE_VIEW_KEY, activeView );
        if ( null != requestToken )
        {
            outState.putSerializable( REQUEST_TOKEN_KEY, requestToken );
        }
        webView.saveState( outState );
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
        webView.loadUrl( authorizationUrl );
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
