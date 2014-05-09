package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.*;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;

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
    protected SitesListAdapter   sitesListAdapter;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sitesSearchHandler.registerReceiver();
    }

    @Override
    public void onPause()
    {
        sitesSearchHandler.unregisterReceiver();
        super.onPause();
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
        activeView = SitesView.READY;
        sitesFooter.activeFooterView = SitesFooter.SitesFooterView.LOAD_OLDER;
        if ( null != savedInstanceState )
        {
            activeView = ( SitesView ) savedInstanceState.getSerializable( ACTIVE_VIEW_KEY );
            sitesFooter.activeFooterView = ( SitesFooter.SitesFooterView ) savedInstanceState.getSerializable( SitesFooter.ACTIVE_FOOTER_VIEW_KEY );
            showView( activeView );
            sitesFooter.showFooterView( sitesFooter.activeFooterView );
        }
        if ( !sitesUserHandler.isUserLoggedIn() )
        {
            showView( SitesView.LOGIN );
        }
        return v;
    }

    private View initViewReady( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewReady = inflater.inflate( R.layout.sites_view_ready, null );
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
        sitesListAdapter = initSitesListAdapter();
        sitesListAdapter.initTypes( savedInstanceState );
        readyHolder.lvResultsList = ( ListView ) viewReady.findViewById( R.id.lv_results_list );
        sitesFooter = initSitesFooter( inflater );
        sitesFooter.appendFooterToList( readyHolder.lvResultsList );
        readyHolder.lvResultsList.setAdapter( sitesListAdapter );
        readyHolder.lvResultsList.setOnItemClickListener( this );
        readyHolder.lvResultsListEmpty = ( TextView ) viewReady.findViewById( R.id.tv_results_list_empty );
        readyHolder.lvResultsList.setEmptyView( readyHolder.lvResultsListEmpty );
        return viewReady;
    }

    private View initViewLoading( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewLoading = inflater.inflate( R.layout.sites_view_loading, null );
        loadingHolder = new Loading();
        loadingHolder.pgbrLoadingResults = ( ProgressBar ) viewLoading.findViewById( R.id.pgbr_loading_results );
        return viewLoading;
    }

    private View initViewLogin( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewLogin = inflater.inflate( R.layout.sites_view_login, null );
        loginHolder = new Login();
        loginHolder.btnLogin = ( Button ) viewLogin.findViewById( R.id.btn_login );
        loginHolder.btnLogin.setText( getLoginButtonText() );
        loginHolder.btnLogin.setOnClickListener( this );
        return viewLogin;
    }

    protected abstract String getLoginButtonText();

    private View initViewError( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewError = inflater.inflate( R.layout.sites_view_error, null );
        errorHolder = new Error();
        errorHolder.tvError = ( TextView ) viewError.findViewById( R.id.tv_error );
        return viewError;
    }

    public SitesFooter initSitesFooter( LayoutInflater inflater )
    {
        SitesFooter sitesFooter = new SitesFooter( inflater, this );
        return sitesFooter;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        if ( item.getItemId() == R.id.it_logout )
        {
            sitesUserHandler.logoutUser();
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onPrepareOptionsMenu( Menu menu )
    {
        menu.findItem( R.id.it_logout ).setVisible( sitesUserHandler.isUserLoggedIn() ? true : false );
    }

    protected abstract SitesUserHandler initSitesUserHandler();

    protected abstract SitesSearchHandler initSitesSearchHandler();

    protected abstract SitesListAdapter initSitesListAdapter();

    protected abstract List<?> initResultsList();

    protected abstract int getLogo();

    protected void searchHashtag( String hashtag )
    {
        if ( !sitesUserHandler.isUserLoggedIn() )
        {
            Toast.makeText( getActivity(), getResources().getString( getNotLoggedInToastTextId() ), Toast.LENGTH_LONG ).show();
            return;
        }
        // We use the listview tag to keep track of selected position. On a new search, we clear this tag.
        readyHolder.lvResultsList.setTag( null );
        sitesSearchHandler.beginSearch( SearchType.INITIAL, hashtag );
    }

    protected abstract int getNotLoggedInToastTextId();

    public void showView( SitesView sitesView )
    {
        vaSitesView.setDisplayedChild( sitesView.index );
        activeView = sitesView;
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putSerializable( ACTIVE_VIEW_KEY, activeView );
        outState.putSerializable( sitesFooter.ACTIVE_FOOTER_VIEW_KEY, sitesFooter.activeFooterView );
        outState.putSerializable( RESULTS_LIST_KEY, ( java.io.Serializable ) results );
        sitesListAdapter.saveTypes( outState );
    }


    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if ( requestCode == getLoginRequestCode() )
        {
            if ( resultCode == Activity.RESULT_OK )
            {
                onUserLoggedIn();
            }
            else
            {
                if ( resultCode == Activity.RESULT_CANCELED )
                {
                    onLoginFailure();
                }
            }
        }
    }

    public void onUserLoggedIn()
    {
        showView( SitesView.READY );
        Toast.makeText( getActivity(), getResources().getString( getLoggedInToastTextId() ) + sitesUserHandler.getUserName(), Toast.LENGTH_LONG ).show();
        getActivity().invalidateOptionsMenu();
    }

    protected abstract int getLoggedInToastTextId();

    public void onLoginFailure()
    {
        showView( SitesView.LOGIN );
        Toast.makeText( getActivity(), getResources().getString( getLoginFailureToastTextId() ), Toast.LENGTH_LONG ).show();
    }

    protected abstract int getLoginFailureToastTextId();

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
        if ( !sitesUserHandler.isUserLoggedIn() )
        {
            Toast.makeText( getActivity(), getResources().getString( getNotLoggedInToastTextId() ), Toast.LENGTH_LONG ).show();
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
        {
            doLogin();
        }
    }

    public void doLogin()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        Intent i = new Intent( getActivity(), getLoginActivityClassName() );
        getActivity().startActivityForResult( i, getLoginRequestCode() );
    }

    protected abstract int getLoginRequestCode();

    protected abstract Class<?> getLoginActivityClassName();

    /**
     * ************** for FooterListener **********************
     */

    @Override
    public void onFooterClicked()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        if ( !sitesUserHandler.isUserLoggedIn() )
        {
            Toast.makeText( getActivity(), getResources().getString( getNotLoggedInToastTextId() ), Toast.LENGTH_LONG ).show();
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
    public void afterSearching( SearchType searchType, List<?> searchResults )
    {
        switch ( searchType )
        {
            case INITIAL:
                afterCurrentSearch( searchResults );
                break;
            case OLDER:
                afterOlderSearch( searchResults );
                break;
            case NEWER:
                afterNewerSearch( searchResults );
                break;
        }
    }

    public void afterCurrentSearch( List<?> searchResults )
    {
        showView( SitesView.READY );
        sitesListAdapter.clear();
        sitesListAdapter.clearTypes();
        if ( !searchResults.isEmpty() )
        {
            // Since we cannot add directly to List<?>, we have to delegate the adding to subclass which
            // casts the list to appropriate type and then does the adding
            addToEnd( searchResults );
            sitesListAdapter.updateTypes( SearchType.INITIAL, searchResults );
            sitesListAdapter.notifyDataSetChanged();
        }
        else
        {
            readyHolder.lvResultsListEmpty.setText( getResources().getString( R.string.str_toast_no_results ) );
        }
    }

    public void afterOlderSearch( List<?> searchResults )
    {
        sitesFooter.showFooterView( SitesFooter.SitesFooterView.LOAD_OLDER );
        if ( !searchResults.isEmpty() )
        {
            // Since we cannot add directly to List<?>, we have to delegate the adding to subclass which
            // casts the list to appropriate type and then does the adding
            addToEnd( searchResults );
            sitesListAdapter.updateTypes( SearchType.OLDER, searchResults );
            sitesListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_older_results ), Toast.LENGTH_LONG ).show();
        }
    }

    protected abstract void addToEnd( List<?> searchResults );

    public void afterNewerSearch( List<?> searchResults )
    {
        readyHolder.srlReady.setRefreshing( false );
        if ( !searchResults.isEmpty() )
        {
            // Since we cannot add directly to List<?>, we have to delegate the adding to subclass which
            // casts the list to appropriate type and then does the adding
            addToStart( searchResults );
            sitesListAdapter.updateTypes( SearchType.NEWER, searchResults );
            sitesListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_newer_results ), Toast.LENGTH_LONG ).show();
        }
    }

    protected abstract void addToStart( List<?> searchResults );

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
                Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_newer_results_error ), Toast.LENGTH_LONG ).show();
                break;
        }
    }

    /*
    *********************** for UserHandlerListener ****************
     */
    public void onUserLoggedOut()
    {
        sitesListAdapter.clear();
        sitesListAdapter.notifyDataSetChanged();
        showView( SitesView.LOGIN );
        getActivity().invalidateOptionsMenu();
    }

    /*
    *****************for List item click ************************
    */

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        Object result = parent.getItemAtPosition( position );
        // We use the tag of the listview to remember position that was last clicked.
        SitesListRow sitesListRow = ( SitesListRow ) view;
        // If tag is null, no position has been expanded yet.
        // Simply show the view for currently selected position and set tag
        if ( null == parent.getTag() )
        {
            sitesListRow.expandRow( result, true );
            parent.setTag( position );
        }
        // If tag is not null, it means we have an already expanded a position...
        else
        {
            int expandedPosition = ( Integer ) parent.getTag();
            // If currently selected position is the same as previously expanded one,
            // we need to hide the view and set tag to null
            if ( position == expandedPosition )
            {
                sitesListRow.collapseRow( true );
                parent.setTag( null );
            }
            // Else the previously selected position is different from our current selected position
            else
            {
                // If the last selected position is visible on screen, then hide it first.
                // If it's not visible, we don't do anything since adapter's getView method will take care of collapsing
                if ( expandedPosition >= parent.getFirstVisiblePosition() && expandedPosition <= parent.getLastVisiblePosition() )
                {
                    ( ( SitesListRow ) parent.getChildAt( expandedPosition - parent.getFirstVisiblePosition() ) ).collapseRow( true );
                }
                // Then show the current selected position and set the tag
                sitesListRow.expandRow( result, true );
                parent.setTag( position );
            }
        }
    }

    protected static class Ready
    {
        public ListView           lvResultsList;
        public TextView           lvResultsListEmpty;
        public SwipeRefreshLayout srlReady;
    }

    protected static class Loading
    {
        public ProgressBar pgbrLoadingResults;
    }

    protected static class Login
    {
        public Button btnLogin;
    }

    protected static class Error
    {
        public TextView tvError;
    }
}
