package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ViewAnimator;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/5/14.
 */
public abstract class LoadingActivity extends FragmentActivity
{
    private static final int    MAIN_VIEW       = 0;
    private static final int    LOADING_VIEW    = 1;
    private static       int    ACTIVE_VIEW     = 0;
    private static final String ACTIVE_VIEW_KEY = HashtaggerApp.NAMESPACE + "active_view_key";
    private ViewAnimator vaLoadingView;
    View mainView;
    View loadingView;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_loading );
        vaLoadingView = ( ViewAnimator ) findViewById( R.id.va_loading_view );
        mainView = initMainView( savedInstanceState );
        loadingView = initLoadingView( savedInstanceState );
        vaLoadingView.addView( mainView, MAIN_VIEW );
        vaLoadingView.addView( loadingView, LOADING_VIEW );
        if ( null != savedInstanceState )
        {
            ACTIVE_VIEW = savedInstanceState.getInt( ACTIVE_VIEW_KEY );
            showView( ACTIVE_VIEW );
        }
        onViewsCreated( savedInstanceState );
    }

    protected abstract View initMainView( Bundle savedInstanceState );

    private View initLoadingView( Bundle savedInstanceState )
    {
        return getLayoutInflater().inflate( R.layout.sites_view_loading, null );
    }

    protected abstract void onViewsCreated( Bundle savedInstanceState );

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
        ACTIVE_VIEW = LOADING_VIEW;
    }

    public void showMainView()
    {
        vaLoadingView.setDisplayedChild( MAIN_VIEW );
        ACTIVE_VIEW = MAIN_VIEW;
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putInt( ACTIVE_VIEW_KEY, ACTIVE_VIEW );
    }
}
