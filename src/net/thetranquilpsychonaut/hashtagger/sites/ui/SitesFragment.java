package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;

/**
 * Created by itwenty on 2/26/14.
 */
public abstract class SitesFragment extends Fragment
{
    private static final String ACTIVE_VIEW_KEY = HashtaggerApp.NAMESPACE + "active_view_key";

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
    protected SitesView          activeView;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        sitesUserHandler = getSitesUserHandler();
        sitesSearchHandler = getSitesSearchHandler();
        View v = inflater.inflate( R.layout.fragment_sites, container, false );
        vaSitesView = ( ViewAnimator ) v.findViewById( R.id.va_sites_view );
        viewReady = fetchView( SitesView.READY, inflater, savedInstanceState );
        viewLoading = fetchView( SitesView.LOADING, inflater, savedInstanceState );
        viewLogin = fetchView( SitesView.LOGIN, inflater, savedInstanceState );
        viewError = fetchView( SitesView.ERROR, inflater, savedInstanceState );
        vaSitesView.addView( viewReady, SitesView.READY.getIndex() );
        vaSitesView.addView( viewLoading, SitesView.LOADING.getIndex() );
        vaSitesView.addView( viewLogin, SitesView.LOGIN.getIndex() );
        vaSitesView.addView( viewError, SitesView.ERROR.getIndex() );
        sitesFooter = getSitesFooter( inflater );
        sitesFooter.activeFooterView = SitesFooter.SitesFooterView.LOAD_OLDER;
        onViewsCreated();
        if ( null != savedInstanceState )
        {
            activeView = ( SitesView ) savedInstanceState.getSerializable( ACTIVE_VIEW_KEY );
            sitesFooter.activeFooterView = ( SitesFooter.SitesFooterView ) savedInstanceState.getSerializable( SitesFooter.ACTIVE_FOOTER_VIEW_KEY );
            showView( activeView );
            sitesFooter.showFooterView( sitesFooter.activeFooterView );
        }
        return v;
    }

    protected abstract SitesUserHandler getSitesUserHandler();

    protected abstract SitesSearchHandler getSitesSearchHandler();

    protected abstract View fetchView( SitesView sitesView, LayoutInflater inflater, Bundle saveInstanceState );

    public abstract SitesFooter getSitesFooter( LayoutInflater inflater );

    protected void onViewsCreated()
    {
    }

    protected abstract void searchHashtag( String hashtag );

    public void showView( SitesView sitesView )
    {
        vaSitesView.setDisplayedChild( sitesView.index );
        activeView = sitesView;
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        Helper.debug( "sitesfragment save instance" );
        super.onSaveInstanceState( outState );
        outState.putSerializable( ACTIVE_VIEW_KEY, activeView );
        outState.putSerializable( sitesFooter.ACTIVE_FOOTER_VIEW_KEY, sitesFooter.activeFooterView );
    }
}
