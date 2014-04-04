package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterUserHandler;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public abstract class SitesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, View.OnClickListener, SitesFooter.SitesFooterListener, SitesSearchHandler.SitesSearchListener, SitesUserHandler.SitesUserListener
{
    private static final String ACTIVE_VIEW_KEY       = HashtaggerApp.NAMESPACE + "active_view_key";
    private static final String RESULTS_LIST_KEY      = HashtaggerApp.NAMESPACE + "results_list_key";
    private static final String SRL_IS_REFRESHING_KEY = HashtaggerApp.NAMESPACE + "srl_is_refreshing_key";

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
    protected Ready              readyHolder;
    protected Loading            loadingHolder;
    protected Login              loginHolder;
    protected Error              errorHolder;
    protected SitesFooter        sitesFooter;
    protected SitesSearchHandler sitesSearchHandler;
    protected SitesUserHandler   sitesUserHandler;
    protected SitesView          activeView;
    protected List<?>            results;
    protected ArrayAdapter<?>    resultsAdapter;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        sitesUserHandler = initSitesUserHandler();
        sitesSearchHandler = initSitesSearchHandler();
        View v = inflater.inflate( R.layout.fragment_sites, container, false );
        vaSitesView = ( ViewAnimator ) v.findViewById( R.id.va_sites_view );
        vaSitesView.addView( initViewReady( inflater, savedInstanceState ), SitesView.READY.getIndex() );
        vaSitesView.addView( initViewLoading( inflater, savedInstanceState ), SitesView.LOADING.getIndex() );
        vaSitesView.addView( initViewLogin( inflater, savedInstanceState ), SitesView.LOGIN.getIndex() );
        vaSitesView.addView( initViewError( inflater, savedInstanceState ), SitesView.ERROR.getIndex() );
        sitesFooter = initSitesFooter( inflater );
        sitesFooter.activeFooterView = SitesFooter.SitesFooterView.LOAD_OLDER;
        if ( null != savedInstanceState )
        {
            activeView = ( SitesView ) savedInstanceState.getSerializable( ACTIVE_VIEW_KEY );
            sitesFooter.activeFooterView = ( SitesFooter.SitesFooterView ) savedInstanceState.getSerializable( SitesFooter.ACTIVE_FOOTER_VIEW_KEY );
            showView( activeView );
            sitesFooter.showFooterView( sitesFooter.activeFooterView );
        }
        if ( !sitesUserHandler.isUserLoggedIn() )
            showView( SitesView.LOGIN );
        return v;
    }

    private View initViewReady( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewReady = inflater.inflate( R.layout.view_ready, null );
        readyHolder = new Ready();
        readyHolder.srlReady = ( SwipeRefreshLayout ) viewReady.findViewById( R.id.srl_ready );
        readyHolder.srlReady.setOnRefreshListener( this );
        readyHolder.srlReady.setColorScheme( android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light );
        if ( null != savedInstanceState )
        {
            results = ( List<?> ) savedInstanceState.getSerializable( RESULTS_LIST_KEY );
            readyHolder.srlReady.setRefreshing( savedInstanceState.getBoolean( SRL_IS_REFRESHING_KEY ) );
        }
        else
        {
            results = initResultsList();
        }
        resultsAdapter = initResultsAdapter();
        readyHolder.lvResultsList = ( ListView ) viewReady.findViewById( R.id.lv_results_list );
        readyHolder.lvResultsList.setAdapter( resultsAdapter );
        readyHolder.lvResultsList.setOnItemClickListener( this );
        readyHolder.lvResultsListEmpty = ( TextView ) viewReady.findViewById( R.id.tv_results_list_empty );
        readyHolder.lvResultsList.setEmptyView( readyHolder.lvResultsListEmpty );
        return viewReady;
    }

    private View initViewLoading( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewLoading = inflater.inflate( R.layout.view_loading, null );
        loadingHolder = new Loading();
        loadingHolder.pgbrLoadingResults = ( ProgressBar ) viewLoading.findViewById( R.id.pgbr_loading_results );
        return viewLoading;
    }

    private View initViewLogin( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewLogin = inflater.inflate( R.layout.view_login, null );
        loginHolder = new Login();
        loginHolder.btnLogin = ( Button ) viewLogin.findViewById( R.id.btn_login );
        loginHolder.btnLogin.setOnClickListener( this );
        return viewLogin;
    }

    private View initViewError( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewError = inflater.inflate( R.layout.view_error, null );
        errorHolder = new Error();
        errorHolder.tvError = ( TextView ) viewError.findViewById( R.id.tv_error );
        return viewError;
    }

    public SitesFooter initSitesFooter( LayoutInflater inflater )
    {
        SitesFooter sitesFooter = new SitesFooter( inflater );
        sitesFooter.setSitesFooterListener( this );
        sitesFooter.appendFooterToList( readyHolder.lvResultsList );
        readyHolder.lvResultsList.setAdapter( resultsAdapter );
        return sitesFooter;
    }

    protected abstract SitesUserHandler initSitesUserHandler();

    protected abstract SitesSearchHandler initSitesSearchHandler();

    protected abstract ArrayAdapter<?> initResultsAdapter();

    protected abstract List<?> initResultsList();

    protected void searchHashtag( String hashtag )
    {
        if ( !sitesUserHandler.isUserLoggedIn() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_twitter_not_logged_in ), Toast.LENGTH_LONG ).show();
            return;
        }
        sitesSearchHandler.beginSearch( SearchType.INITIAL, hashtag );
    }

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


    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        Helper.debug( "onActivityResult" );
        if ( requestCode == HashtaggerApp.LOGIN_REQUEST_CODE )
        {
            if ( resultCode == Activity.RESULT_OK )
            {
                onUserLoggedIn();
            }
            else if ( requestCode == Activity.RESULT_CANCELED )
            {
                onLoginFailure();
            }
        }
    }

    public void onUserLoggedIn()
    {
        showView( SitesView.READY );
        Toast.makeText( getActivity(), "Logged in to Twitter as " + sitesUserHandler.getUserName(), Toast.LENGTH_LONG ).show();
        getActivity().invalidateOptionsMenu();
    }

    public void onLoginFailure()
    {
        Toast.makeText( getActivity(), "Could not log you in. Please try again.", Toast.LENGTH_LONG ).show();
        showView( SitesView.LOGIN );
    }

    /**
     * **************** for onRefreshListener **************
     */

    @Override
    public void onRefresh()
    {
        doLoadNewerResults();
    }

    private void doLoadNewerResults()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        sitesSearchHandler.beginSearch( SearchType.NEWER, ( ( SitesActivity ) getActivity() ).getHashtag() );
    }

    /**
     * **************** for OnClickListener *********************
     */

    @Override
    public void onClick( View v )
    {
        if ( v.equals( loginHolder.btnLogin ) )
            doLogin();
    }

    public void doLogin()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        Intent i = new Intent( getActivity(), getLoginActivityClassName() );
        getActivity().startActivityForResult( i, HashtaggerApp.LOGIN_REQUEST_CODE );
    }

    protected abstract Class<?> getLoginActivityClassName();

    /**
     * ************** for FooterListener **********************
     */

    @Override
    public void onFooterClicked()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_twitter_not_logged_in ), Toast.LENGTH_LONG ).show();
            return;
        }
        sitesSearchHandler.beginSearch( SearchType.OLDER, ( ( SitesActivity ) getActivity() ).getHashtag() );
    }

    /**
     * ****************for SearchHandlerListener *************
     */

    @Override
    public void whileSearching( SearchType searchType )
    {
        switch ( searchType )
        {
            case INITIAL:
                showView( SitesView.LOADING );
                break;
            case OLDER:
                sitesFooter.showFooterView( SitesFooter.SitesFooterView.LOADING );
                break;
            case NEWER:
                readyHolder.srlReady.setRefreshing( true );
                break;
        }
    }

    @Override
    public void afterSearching( SearchType searchType, Bundle resultBundle )
    {
        List<?> results = ( List<?> ) resultBundle.getSerializable( HashtaggerApp.SEARCH_RESULT_LIST_KEY );
        switch ( searchType )
        {
            case INITIAL:
                afterCurrentSearch( results );
                break;
            case OLDER:
                afterOlderSearch( results );
                break;
            case NEWER:
                afterNewerSearch( results );
                break;
        }
    }

    public void afterCurrentSearch( List<?> statuses )
    {
        showView( SitesView.READY );
        resultsAdapter.clear();
        if ( !statuses.isEmpty() )
        {
            addToEnd( statuses );
            resultsAdapter.notifyDataSetChanged();
        }
        else
        {
            readyHolder.lvResultsListEmpty.setText( getResources().getString( R.string.str_toast_no_results ) );
        }
    }

    public void afterOlderSearch( List<?> statuses )
    {
        sitesFooter.showFooterView( SitesFooter.SitesFooterView.LOAD_OLDER );
        if ( null != statuses )
        {
            addToEnd( statuses );
            resultsAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_older_results ), Toast.LENGTH_LONG ).show();
        }

    }

    protected abstract void addToEnd( List<?> statuses );

    public void afterNewerSearch( List<?> statuses )
    {
        readyHolder.srlReady.setRefreshing( false );
        if ( null != statuses )
        {
            addToStart( statuses );
            resultsAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_newer_results ), Toast.LENGTH_LONG ).show();
        }
    }

    protected abstract void addToStart( List<?> statuses );

    @Override
    public void onError( SearchType searchType )
    {
        switch ( searchType )
        {
            case INITIAL:
                showView( SitesView.ERROR );
                break;
            case OLDER:
                sitesFooter.showFooterView( SitesFooter.SitesFooterView.ERROR );
                break;
            case NEWER:
                readyHolder.srlReady.setRefreshing( false );
                Toast.makeText( getActivity(), "Failed to load newer results", Toast.LENGTH_LONG ).show();
                break;
        }
    }

    /*
    *********************** for UserHandlerListener ****************
     */
    public void onUserLoggedOut()
    {
        resultsAdapter.clear();
        resultsAdapter.notifyDataSetChanged();
        getActivity().setTitle( getResources().getString( R.string.app_name ) );
        showView( SitesView.LOGIN );
        getActivity().invalidateOptionsMenu();
    }

    protected static class Ready
    {
        public Ready()
        {
        }

        ;
        public ListView           lvResultsList;
        public TextView           lvResultsListEmpty;
        public SwipeRefreshLayout srlReady;
    }

    protected static class Loading
    {
        public Loading()
        {
        }

        ;
        public ProgressBar pgbrLoadingResults;
    }

    protected static class Login
    {
        public Login()
        {
        }

        ;
        public Button btnLogin;
    }

    protected static class Error
    {
        public Error()
        {
        }

        public TextView tvError;
    }
}
