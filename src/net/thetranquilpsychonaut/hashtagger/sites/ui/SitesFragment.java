package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.utils.DefaultPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.MySwipeRefreshLayout;

import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public abstract class SitesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, SitesSearchHandler.SitesSearchListener, SitesUserHandler.SitesUserListener, AdapterView.OnItemLongClickListener
{
    private static final String ACTIVE_VIEW_KEY    = HashtaggerApp.NAMESPACE + "active_view_key";
    private static final String RESULTS_LIST_KEY   = HashtaggerApp.NAMESPACE + "results_list_key";
    private static final String FOOTER_MODE_KEY    = HashtaggerApp.NAMESPACE + "footer_mode";
    private static final String BAR_VISIBILITY_KEY = HashtaggerApp.NAMESPACE + "bar_visibility";

    private static final int AUTO_UPDATE_INTERVAL = 1000 * 30; // 30 seconds

    private static final int READY   = 0;
    private static final int LOADING = 1;
    private static final int LOGIN   = 2;
    private static final int ERROR   = 3;

    private   ViewAnimator       vaSitesView;
    protected Ready              readyHolder;
    protected Loading            loadingHolder;
    protected Login              loginHolder;
    protected Error              errorHolder;
    protected SitesSearchHandler sitesSearchHandler;
    protected SitesUserHandler   sitesUserHandler;
    protected List<?>            results;
    protected SitesListAdapter   sitesListAdapter;
    protected Handler            timedSearchHandler;
    protected TimedSearchRunner  timedSearchRunner;

    protected int activeView = READY;

    private class TimedSearchRunner implements Runnable
    {
        @Override
        public void run()
        {
            // Ensure that getEnteredHashtag() does not return null before calling this.
            sitesSearchHandler.beginSearch( SearchType.TIMED, getEnteredHashtag() );
        }
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );
        timedSearchHandler = new Handler( Looper.getMainLooper() );
        timedSearchRunner = new TimedSearchRunner();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        sitesSearchHandler.registerReceiver();
        if ( !sitesSearchHandler.isSearchRunning() )
        {
            if ( activeView == LOADING )
            {
                showView( READY );
                showClickHashtagIfAlreadyEntered();
            }
            if ( readyHolder.srlReady.isRefreshing() )
            {
                readyHolder.srlReady.setRefreshing( false );
            }
            if ( readyHolder.sitesFooterView.getMode() == readyHolder.sitesFooterView.LOADING )
            {
                readyHolder.sitesFooterView.setMode( readyHolder.sitesFooterView.NORMAL );
            }
        }
        if ( !TextUtils.isEmpty( getEnteredHashtag() ) && !results.isEmpty() )
        {
            postNextTimedSearch();
        }
    }

    private void postNextTimedSearch()
    {
        Helper.debug( "Auto update is : " + DefaultPrefs.autoUpdate );
        if ( DefaultPrefs.autoUpdate )
        {
            Helper.debug( "Next timed search posted" );
            timedSearchHandler.removeCallbacks( timedSearchRunner );
            timedSearchHandler.postDelayed( timedSearchRunner, AUTO_UPDATE_INTERVAL );
        }
    }

    @Override
    public void onStop()
    {
        timedSearchHandler.removeCallbacks( timedSearchRunner );
        sitesSearchHandler.unregisterReceiver();
        super.onStop();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        sitesUserHandler = initSitesUserHandler();
        sitesSearchHandler = initSitesSearchHandler();
        View v = inflater.inflate( R.layout.fragment_sites, container, false );
        vaSitesView = ( ViewAnimator ) v.findViewById( R.id.va_sites_view );
        vaSitesView.addView( initViewReady( inflater, savedInstanceState ), READY );
        vaSitesView.addView( initViewLoading( inflater, savedInstanceState ), LOADING );
        vaSitesView.addView( initViewLogin( inflater, savedInstanceState ), LOGIN );
        vaSitesView.addView( initViewError( inflater, savedInstanceState ), ERROR );
        showView( null == savedInstanceState ? READY : savedInstanceState.getInt( ACTIVE_VIEW_KEY ) );
        if ( !sitesUserHandler.isUserLoggedIn() )
        {
            showView( LOGIN );
        }
        return v;
    }

    private View initViewReady( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewReady = inflater.inflate( R.layout.sites_view_ready, null );
        readyHolder = new Ready();

        // Non view members whose state needs to be saved
        initResultsList( savedInstanceState );
        initSitesListAdapter( savedInstanceState );

        // View members whose state needs to be saved are restored in restoreViewStates()
        initSwipeRefreshLayout( viewReady );
        initSitesFooterView( viewReady );
        initResultListEmptyView( viewReady );
        initNewResultsBar( viewReady );
        initResultsListView( viewReady );
        if ( null != savedInstanceState )
        {
            restoreViewStates( savedInstanceState );
        }
        return viewReady;

    }

    private void initResultsList( Bundle savedInstanceState )
    {
        results = null == savedInstanceState ? initResultsList() : ( List<?> ) savedInstanceState.getSerializable( RESULTS_LIST_KEY );
    }

    private void initSitesListAdapter( Bundle savedInstanceState )
    {
        sitesListAdapter = initSitesListAdapter();
        sitesListAdapter.initTypes( savedInstanceState );
    }

    private void initSwipeRefreshLayout( View viewReady )
    {
        readyHolder.srlReady = ( MySwipeRefreshLayout ) viewReady.findViewById( R.id.srl_ready );
        readyHolder.srlReady.setOnRefreshListener( this );
        readyHolder.srlReady.setColorScheme( android.R.color.holo_blue_light,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_blue_dark );
    }

    private void initSitesFooterView( View viewReady )
    {
        readyHolder.sitesFooterView = new SitesFooterView( viewReady.getContext() );
        readyHolder.sitesFooterView.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                if ( !TextUtils.isEmpty( getEnteredHashtag() ) )
                {
                    doLoadOlderResults( getEnteredHashtag() );
                }
            }
        } );
    }

    private void initResultListEmptyView( View viewReady )
    {
        readyHolder.lvResultsListEmpty = ( TextView ) viewReady.findViewById( R.id.tv_results_list_empty );
        readyHolder.lvResultsListEmpty.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                searchHashtagIfAlreadyEntered();
            }
        } );
    }

    private void initNewResultsBar( View viewReady )
    {
        readyHolder.newResultsBar = ( NewResultsBar ) viewReady.findViewById( R.id.new_results_bar );
        readyHolder.newResultsBar.setOnScrollToNewClickListener( new NewResultsBar.OnScrollToNewClickListener()
        {
            @Override
            public void onScrollToNewClicked( final NewResultsBar bar, int resultCount )
            {
                if ( null == readyHolder.lvResultsList )
                    return;

                final int currentPosition = readyHolder.lvResultsList.getFirstVisiblePosition();
                if ( currentPosition > resultCount )
                {
                    // We set a new onScrollListener to notify us when scroll stops
                    readyHolder.lvResultsList.setOnScrollListener( new AbsListView.OnScrollListener()
                    {
                        @Override
                        public void onScrollStateChanged( AbsListView view, int scrollState )
                        {
                            if ( scrollState == SCROLL_STATE_IDLE )
                            {
                                // When the scroll ends and results count is reached,
                                // we call expandNewResultsText() to make user aware of new results above
                                // this point and restore the scroll listener to its previous value
                                bar.expandNewResultsText();
                                readyHolder.lvResultsList.setOnScrollListener( bar );
                            }
                        }

                        // No need to override this method
                        @Override
                        public void onScroll( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount )
                        {

                        }
                    } );
                    // Now we actually perform the scroll upto results count
                    // We need an offset certain pixels because without it, getFirstVisiblePosition()
                    // reports one position less than the position actually visible, causing NewResultsBar count
                    // to decrease by one.
                    readyHolder.lvResultsList.smoothScrollToPositionFromTop( resultCount, -5 );
                }
                else
                    readyHolder.lvResultsList.smoothScrollToPosition( 0 );
            }
        } );
    }

    private void initResultsListView( View viewReady )
    {
        readyHolder.lvResultsList = ( ListView ) viewReady.findViewById( R.id.lv_results_list );
        readyHolder.lvResultsList.addFooterView( readyHolder.sitesFooterView, null, false );
        readyHolder.lvResultsList.setAdapter( sitesListAdapter );
        readyHolder.lvResultsList.setEmptyView( readyHolder.lvResultsListEmpty );
        readyHolder.lvResultsList.setOnItemClickListener( this );
        readyHolder.lvResultsList.setOnItemLongClickListener( this );
        readyHolder.lvResultsList.setOnScrollListener( readyHolder.newResultsBar );
    }

    private View initViewLoading( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewLoading = inflater.inflate( R.layout.sites_view_loading, null );
        loadingHolder = new Loading();
        loadingHolder.pgbrLoadingResults = ( ProgressBar ) viewLoading.findViewById( R.id.pgbr_loading_results );
        return viewLoading;
    }

    private void restoreViewStates( Bundle savedInstanceState )
    {
        readyHolder.sitesFooterView.setMode( savedInstanceState.getInt( FOOTER_MODE_KEY ) );
        readyHolder.newResultsBar.setVisibility( savedInstanceState.getInt( BAR_VISIBILITY_KEY ) );
    }

    private View initViewLogin( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewLogin = inflater.inflate( R.layout.sites_view_login, null );
        loginHolder = new Login();
        loginHolder.btnLogin = ( Button ) viewLogin.findViewById( R.id.btn_login );
        loginHolder.btnLogin.setText( getLoginButtonText() );
        loginHolder.btnLogin.setBackgroundResource( getLoginButtonBackgroundId() );
        loginHolder.btnLogin.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                doLogin();
            }
        } );
        return viewLogin;
    }

    protected abstract int getLoginButtonBackgroundId();

    private View initViewError( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewError = inflater.inflate( R.layout.sites_view_error, null );
        errorHolder = new Error();
        errorHolder.tvError = ( TextView ) viewError.findViewById( R.id.tv_error );
        return viewError;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        super.onActivityCreated( savedInstanceState );
        showClickHashtagIfAlreadyEntered();
    }

    private void showClickHashtagIfAlreadyEntered()
    {
        String hashtag = getEnteredHashtag();
        if ( !TextUtils.isEmpty( hashtag ) )
        {
            readyHolder.lvResultsListEmpty.setText( "Click to search for " + hashtag );
        }
    }

    private void searchHashtagIfAlreadyEntered()
    {
        String hashtag = getEnteredHashtag();
        if ( !TextUtils.isEmpty( hashtag ) )
        {
            searchHashtag( hashtag );
        }
    }

    private String getEnteredHashtag()
    {
        return null != getActivity() && !TextUtils.isEmpty( ( ( SitesActivity ) getActivity() ).getHashtag() ) ? ( ( SitesActivity ) getActivity() ).getHashtag() : null;
    }

    protected abstract String getLoginButtonText();

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        if ( item.getItemId() == R.id.it_logout )
        {
            doLogout();
            return true;
        }
        else
        {
            return false;
        }
    }

    private void doLogout()
    {
        new AlertDialog.Builder( getActivity() )
                .setTitle( "Logout?" )
                .setMessage( "Are you sure you want to logout?" )
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        sitesUserHandler.logoutUser();
                    }
                } )
                .setNegativeButton( "No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        dialog.dismiss();
                    }
                } )
                .show();
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
            loginHolder.btnLogin.animate().rotationX( loginHolder.btnLogin.getRotationX() == 0 ? 360 : 0 ).setDuration( 1000 ).start();
            return;
        }
        timedSearchHandler.removeCallbacks( timedSearchRunner );
        // We use the listview tag to keep track of selected position. On a new search, we clear this tag.
        readyHolder.lvResultsList.setTag( null );
        sitesListAdapter.clear();
        sitesListAdapter.clearTypes();
        sitesSearchHandler.beginSearch( SearchType.INITIAL, hashtag );
    }

    public void showView( int activeView )
    {
        vaSitesView.setDisplayedChild( activeView );
        this.activeView = activeView;
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putSerializable( RESULTS_LIST_KEY, ( java.io.Serializable ) results );
        outState.putInt( ACTIVE_VIEW_KEY, activeView );
        outState.putInt( FOOTER_MODE_KEY, readyHolder.sitesFooterView.getMode() );
        outState.putInt( BAR_VISIBILITY_KEY, readyHolder.newResultsBar.getVisibility() );
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
            else if ( resultCode == Activity.RESULT_CANCELED )
            {
                onLoginFailure();
            }
        }
    }

    public void onUserLoggedIn()
    {
        showView( READY );
        Toast.makeText( getActivity(), getResources().getString( getLoggedInToastTextId() ) + sitesUserHandler.getUserName(), Toast.LENGTH_LONG ).show();
        getActivity().invalidateOptionsMenu();
        showClickHashtagIfAlreadyEntered();
    }


    protected abstract int getLoggedInToastTextId();

    public void onLoginFailure()
    {
        showView( LOGIN );
        Toast.makeText( getActivity(), getResources().getString( getLoginFailureToastTextId() ), Toast.LENGTH_LONG ).show();
    }

    protected abstract int getLoginFailureToastTextId();

    /**
     * **************** for onRefreshListener **************
     */

    @Override
    public void onRefresh()
    {
        if ( !TextUtils.isEmpty( getEnteredHashtag() ) )
        {
            doLoadNewerResults( getEnteredHashtag() );
        }
    }

    private void doLoadNewerResults( String hashtag )
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( getActivity() );
            return;
        }
        if ( !sitesUserHandler.isUserLoggedIn() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_not_logged_in ), Toast.LENGTH_LONG ).show();
            return;
        }
        sitesSearchHandler.beginSearch( SearchType.NEWER, hashtag );
    }

    public void doLogin()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( getActivity() );
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

    public void doLoadOlderResults( String hashtag )
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( getActivity() );
            return;
        }
        if ( !sitesUserHandler.isUserLoggedIn() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_not_logged_in ), Toast.LENGTH_LONG ).show();
            return;
        }
        sitesSearchHandler.beginSearch( SearchType.OLDER, hashtag );
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
                showView( LOADING );
                break;
            case OLDER:
                readyHolder.sitesFooterView.setMode( SitesFooterView.LOADING );
                break;
            case NEWER:
                readyHolder.srlReady.setRefreshing( true );
                break;
            case TIMED:
                break;
        }
    }

    @Override
    public void afterSearching( SearchType searchType, List<?> searchResults )
    {
        switch ( searchType )
        {
            case INITIAL:
                afterInitialSearch( searchResults );
                break;
            case OLDER:
                afterOlderSearch( searchResults );
                break;
            case NEWER:
                afterNewerSearch( searchResults );
                break;
            case TIMED:
                afterTimedSearch( searchResults );
                break;
        }
    }

    public void afterInitialSearch( List<?> searchResults )
    {
        showView( READY );
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
            readyHolder.lvResultsListEmpty.setOnClickListener( null );
        }
        postNextTimedSearch();
    }

    public void afterOlderSearch( List<?> searchResults )
    {
        readyHolder.sitesFooterView.setMode( SitesFooterView.NORMAL );
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
            int newFirstVisiblePositionIndex = readyHolder.lvResultsList.getFirstVisiblePosition() + searchResults.size();
            int topOffset = null == readyHolder.lvResultsList.getChildAt( 0 ) ? 0 : readyHolder.lvResultsList.getChildAt( 0 ).getTop();
            // Since we cannot add directly to List<?>, we have to delegate the adding to subclass which
            // casts the list to appropriate type and then does the adding
            addToStart( searchResults );
            sitesListAdapter.updateTypes( SearchType.NEWER, searchResults );
            sitesListAdapter.notifyDataSetChanged();
            readyHolder.lvResultsList.setSelectionFromTop( newFirstVisiblePositionIndex, topOffset );
            readyHolder.newResultsBar.setVisibility( View.VISIBLE );
            readyHolder.newResultsBar.setResultsCount( readyHolder.newResultsBar.getResultsCount() + searchResults.size() );
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_newer_results ), Toast.LENGTH_LONG ).show();
        }
    }

    protected abstract void addToStart( List<?> searchResults );

    public void afterTimedSearch( List<?> searchResults )
    {
        if ( !searchResults.isEmpty() )
        {
            int newFirstVisiblePositionIndex = readyHolder.lvResultsList.getFirstVisiblePosition() + searchResults.size();
            int topOffset = null == readyHolder.lvResultsList.getChildAt( 0 ) ? 0 : readyHolder.lvResultsList.getChildAt( 0 ).getTop();
            // Since we cannot add directly to List<?>, we have to delegate the adding to subclass which
            // casts the list to appropriate type and then does the adding
            addToStart( searchResults );
            sitesListAdapter.updateTypes( SearchType.NEWER, searchResults );
            sitesListAdapter.notifyDataSetChanged();
            readyHolder.lvResultsList.setSelectionFromTop( newFirstVisiblePositionIndex, topOffset );
            readyHolder.newResultsBar.setVisibility( View.VISIBLE );
            readyHolder.newResultsBar.setResultsCount( readyHolder.newResultsBar.getResultsCount() + searchResults.size() );
        }
        postNextTimedSearch();
    }

    @Override
    public void onError( SearchType searchType )
    {
        switch ( searchType )
        {
            case INITIAL:
                showView( ERROR );
                break;
            case OLDER:
                readyHolder.sitesFooterView.setMode( SitesFooterView.ERROR );
                break;
            case NEWER:
                readyHolder.srlReady.setRefreshing( false );
                Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_newer_results_error ), Toast.LENGTH_LONG ).show();
                break;
            case TIMED:
                postNextTimedSearch();
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
        showView( LOGIN );
        getActivity().invalidateOptionsMenu();
    }

    /*
    *****************for List item click ************************
    */

    @Override
    public void onItemClick( AdapterView<?> parent, final View view, int position, long id )
    {
        Object result = parent.getItemAtPosition( position );
        // We use the tag of the listview to remember position that was last clicked.
        SitesListRow sitesListRow = ( SitesListRow ) view;
        AnimatorListenerAdapter adapter = new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd( Animator animation )
            {
                scrollIfHidden( view );
            }
        };
        // If tag is null, no position has been expanded yet.
        // Simply show the view for currently selected position and set tag
        if ( null == parent.getTag() )
        {
            sitesListRow.expandRow( result, true, adapter );
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
                sitesListRow.expandRow( result, true, adapter );
                parent.setTag( position );
            }
        }
    }

    private void scrollIfHidden( View view )
    {
        Rect r = new Rect();
        view.getLocalVisibleRect( r );
        int hiddenHeight = view.getHeight() - r.height();
        if ( ( hiddenHeight > 0 ) &&
                ( readyHolder.lvResultsList.getLastVisiblePosition() == readyHolder.lvResultsList.getPositionForView( view ) ) )
        {
            readyHolder.lvResultsList.smoothScrollBy( hiddenHeight, 2 * hiddenHeight );
        }
    }

    @Override
    public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id )
    {
        Intent i = new Intent( Intent.ACTION_VIEW );
        i.setData( getResultUrl( parent.getItemAtPosition( position ) ) );
        getActivity().startActivity( i );
        return true;
    }

    protected abstract Uri getResultUrl( Object result );

    protected static class Ready
    {
        public ListView             lvResultsList;
        public TextView             lvResultsListEmpty;
        public SitesFooterView      sitesFooterView;
        public MySwipeRefreshLayout srlReady;
        public NewResultsBar        newResultsBar;
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
