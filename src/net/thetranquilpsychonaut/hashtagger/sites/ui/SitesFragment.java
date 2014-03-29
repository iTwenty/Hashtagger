package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.exception.NoNetworkException;
import net.thetranquilpsychonaut.hashtagger.exception.NotLoggedInException;
import net.thetranquilpsychonaut.hashtagger.otto.HashtagEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;

/**
 * Created by itwenty on 2/26/14.
 */
public abstract class SitesFragment extends Fragment
{
    public static enum SitesView
    {
        READY( 0 ), LOADING( 1 ), LOGIN( 2 ), ERROR( 3 );
        private int index;

        SitesView( int index )
        {
            this.index = index;
        }

        public int getIndex()
        {
            return index;
        }
    }

    private   ViewAnimator       vaSitesView;
    protected View               viewReady;
    protected SitesFooter        sitesFooter;
    protected View               viewLoading;
    protected View               viewLogin;
    protected View               viewError;
    protected SitesSearchHandler sitesSearchHandler;
    protected SitesUserHandler   sitesUserHandler;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        HashtaggerApp.bus.register( this );
        onInitialize();
        sitesUserHandler = getSitesUserHandler();
        sitesSearchHandler = getSitesSearchHandler();
        onHandlersCreated();
        View v = inflater.inflate( R.layout.fragment_sites, container, false );
        vaSitesView = ( ViewAnimator ) v.findViewById( R.id.va_sites_view );
        viewReady = fetchView( SitesView.READY, inflater );
        viewLoading = fetchView( SitesView.LOADING, inflater );
        viewLogin = fetchView( SitesView.LOGIN, inflater );
        viewError = fetchView( SitesView.ERROR, inflater );
        vaSitesView.addView( viewReady, SitesView.READY.getIndex() );
        vaSitesView.addView( viewLoading, SitesView.LOADING.getIndex() );
        vaSitesView.addView( viewLogin, SitesView.LOGIN.getIndex() );
        vaSitesView.addView( viewError, SitesView.ERROR.getIndex() );
        sitesFooter = getSitesFooter( inflater );
        onViewsCreated();
        return v;
    }

    protected void onInitialize()
    {
    }

    protected abstract SitesUserHandler getSitesUserHandler();

    protected abstract SitesSearchHandler getSitesSearchHandler();

    protected void onHandlersCreated()
    {
    }

    protected abstract View fetchView( SitesView sitesView, LayoutInflater inflater );

    public abstract SitesFooter getSitesFooter( LayoutInflater inflater );

    protected void onViewsCreated()
    {
    }

    @Subscribe
    protected abstract void searchHashtag( HashtagEvent event );

    protected void ensureNetworkConnected() throws NoNetworkException
    {
        if ( !HashtaggerApp.isNetworkConnected() )
            throw new NoNetworkException( "Please connect to a network first." );
    }

    protected abstract void ensureUserLoggedIn() throws NotLoggedInException;

    public void showView( SitesView sitesView )
    {
        vaSitesView.setDisplayedChild( sitesView.index );
    }
}
