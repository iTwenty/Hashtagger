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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewAnimator;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.events.SearchHashtagEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.utils.DefaultPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.MySwipeRefreshLayout;

import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public abstract class SitesFragment extends Fragment implements AdapterView.OnItemClickListener, SitesSearchHandler.SitesSearchListener, AdapterView.OnItemLongClickListener
{
    private static final String ACTIVE_VIEW_KEY        = "active_view";
    private static final String ACTIVE_FOOTER_VIEW_KEY = "active_footer_view";

    private static final int AUTO_UPDATE_INTERVAL = 1000 * 30; // 30 seconds

    private static final int READY   = 0;
    private static final int LOADING = 1;
    private static final int LOGIN   = 2;

    private   ViewAnimator       vaSitesView;
    protected ViewHolder         viewHolder;
    protected SitesSearchHandler sitesSearchHandler;
    protected List<?>            results;
    protected List<Integer>      resultTypes;
    protected SitesListAdapter   sitesListAdapter;
    protected Handler            timedSearchHandler;
    protected TimedSearchRunner  timedSearchRunner;
    protected Animation          fadeIn;
    protected Animation          fadeOut;

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
        results = initResultsList();
        resultTypes = initResultTypesList();
        sitesListAdapter = initSitesListAdapter();
        sitesSearchHandler = initSitesSearchHandler();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        sitesSearchHandler.registerReceiver();
        HashtaggerApp.bus.register( this );
        if ( !sitesSearchHandler.isSearchRunning() )
        {
            if ( activeView == LOADING )
            {
                showView( READY );
                showClickHashtagIfAlreadyEntered();
            }
            if ( viewHolder.srlReady.isRefreshing() )
            {
                viewHolder.srlReady.setRefreshing( false );
            }
            if ( viewHolder.sitesFooterView.getActiveView() == viewHolder.sitesFooterView.LOADING )
            {
                viewHolder.sitesFooterView.showView( viewHolder.sitesFooterView.NORMAL );
            }
        }
        if ( !TextUtils.isEmpty( getEnteredHashtag() ) && !results.isEmpty() )
        {
            postNextTimedSearch();
        }
    }

    private void postNextTimedSearch()
    {
        if ( DefaultPrefs.autoUpdate )
        {
            timedSearchHandler.removeCallbacks( timedSearchRunner );
            timedSearchHandler.postDelayed( timedSearchRunner, AUTO_UPDATE_INTERVAL );
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        timedSearchHandler.removeCallbacks( timedSearchRunner );
        sitesSearchHandler.unregisterReceiver();
        HashtaggerApp.bus.unregister( this );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_sites, container, false );
        vaSitesView = ( ViewAnimator ) v.findViewById( R.id.va_sites_view );
        viewHolder = new ViewHolder();
        vaSitesView.addView( initViewReady( inflater ), READY );
        vaSitesView.addView( initViewLoading( inflater ), LOADING );
        vaSitesView.addView( initViewLogin( inflater ), LOGIN );
        return v;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState )
    {
        super.onViewCreated( view, savedInstanceState );
        restoreViewStates( savedInstanceState );
        if ( !isUserLoggedIn() )
        {
            showView( LOGIN );
        }
    }

    private View initViewReady( LayoutInflater inflater )
    {
        View viewReady = inflater.inflate( R.layout.sites_view_ready, null );
        initSwipeRefreshLayout( viewReady );
        initSitesFooterView( viewReady );
        initSitesEmptyView( viewReady );
        initNewResultsBar( viewReady );
        initResultsListView( viewReady );
        return viewReady;
    }

    private void initSwipeRefreshLayout( View viewReady )
    {
        viewHolder.srlReady = ( MySwipeRefreshLayout ) viewReady.findViewById( R.id.srl_ready );
        viewHolder.srlReady.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if ( !TextUtils.isEmpty( getEnteredHashtag() ) )
                {
                    doLoadNewerResults( getEnteredHashtag() );
                }
            }
        } );
        viewHolder.srlReady.setColorScheme( android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark );
    }

    private void initSitesFooterView( View viewReady )
    {
        viewHolder.sitesFooterView = new SitesFooterView( viewReady.getContext() );
        viewHolder.sitesFooterView.setOnClickListener( new View.OnClickListener()
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

    private void initSitesEmptyView( View viewReady )
    {
        viewHolder.sitesEmptyView = ( SitesEmptyView ) viewReady.findViewById( R.id.sites_empty_view );
        viewHolder.sitesEmptyView.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                if ( TextUtils.isEmpty( getEnteredHashtag() ) )
                {
                    ( ( SitesActivity ) getActivity() ).svHashtag.onActionViewExpanded();
                }
                else
                {
                    searchHashtag( null );
                }
            }
        } );
        viewHolder.sitesEmptyView.setText( "Feels a bit empty here. Try the search icon above?" );
        viewHolder.sitesEmptyView.setImage( getSketchLogoResId() );
    }

    private void initNewResultsBar( View viewReady )
    {
        viewHolder.newResultsBar = ( NewResultsBar ) viewReady.findViewById( R.id.new_results_bar );
        viewHolder.newResultsBar.setOnScrollToNewClickListener( new NewResultsBar.OnScrollToNewClickListener()
        {
            @Override
            public void onScrollToNewClicked( final NewResultsBar bar, int resultCount )
            {
                if ( null == viewHolder.lvResultsList )
                {
                    return;
                }

                final int currentPosition = viewHolder.lvResultsList.getFirstVisiblePosition();
                if ( currentPosition > resultCount )
                {
                    // We temporarily set a new onScrollListener to notify us when scroll stops
                    viewHolder.lvResultsList.setOnScrollListener( new AbsListView.OnScrollListener()
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
                                viewHolder.lvResultsList.setOnScrollListener( bar );
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
                    viewHolder.lvResultsList.smoothScrollToPositionFromTop( resultCount, -5 );
                }
                else
                {
                    viewHolder.lvResultsList.smoothScrollToPosition( 0 );
                }
            }
        } );
    }

    private void initResultsListView( View viewReady )
    {
        viewHolder.lvResultsList = ( ListView ) viewReady.findViewById( R.id.lv_results_list );
        viewHolder.lvResultsList.addFooterView( viewHolder.sitesFooterView, null, false );
        viewHolder.lvResultsList.setAdapter( sitesListAdapter );
        viewHolder.lvResultsList.setEmptyView( viewHolder.sitesEmptyView );
        viewHolder.lvResultsList.setOnItemClickListener( this );
        viewHolder.lvResultsList.setOnItemLongClickListener( this );
        viewHolder.lvResultsList.setOnScrollListener( viewHolder.newResultsBar );
    }

    private View initViewLoading( LayoutInflater inflater )
    {
        View viewLoading = inflater.inflate( R.layout.sites_view_loading, null );
        viewHolder.pgbrLoadingResults = ( ProgressBar ) viewLoading.findViewById( R.id.pgbr_loading_results );
        return viewLoading;
    }

    private View initViewLogin( LayoutInflater inflater )
    {
        View viewLogin = inflater.inflate( R.layout.sites_view_login, null );
        viewHolder.btnLogin = ( Button ) viewLogin.findViewById( R.id.btn_login );
        viewHolder.btnLogin.setText( getLoginButtonText() );
        viewHolder.btnLogin.setBackgroundResource( getLoginButtonBackgroundId() );
        viewHolder.btnLogin.setOnClickListener( new View.OnClickListener()
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

    private void restoreViewStates( Bundle savedInstanceState )
    {
        if ( null != savedInstanceState )
        {
            showView( savedInstanceState.getInt( ACTIVE_VIEW_KEY ) );
            viewHolder.sitesFooterView.showView( savedInstanceState.getInt( ACTIVE_FOOTER_VIEW_KEY ) );
        }
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        super.onActivityCreated( savedInstanceState );
        showClickHashtagIfAlreadyEntered();
        fadeIn = AnimationUtils.loadAnimation( getActivity(), android.R.anim.fade_in );
        fadeOut = AnimationUtils.loadAnimation( getActivity(), android.R.anim.fade_out );
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putInt( ACTIVE_VIEW_KEY, getActiveView() );
        outState.putInt( ACTIVE_FOOTER_VIEW_KEY, viewHolder.sitesFooterView.getActiveView() );
        saveData();
    }

    protected abstract void saveData();

    private void showClickHashtagIfAlreadyEntered()
    {
        if ( !TextUtils.isEmpty( getEnteredHashtag() ) )
        {
            viewHolder.sitesEmptyView.setText( "Click to search for " + getEnteredHashtag() );
        }
    }

    protected String getEnteredHashtag()
    {
        return SitesActivity.currentHashtag;
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
                .setIcon( getLogoResId() )
                .setTitle( getSiteName() )
                .setMessage( "Are you sure you want to logout?" )
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        logoutUser();
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

    protected abstract String getSiteName();

    @Override
    public void onPrepareOptionsMenu( Menu menu )
    {
        menu.findItem( R.id.it_logout ).setVisible( isUserLoggedIn() ? true : false );
    }

    protected abstract SitesSearchHandler initSitesSearchHandler();

    protected abstract SitesListAdapter initSitesListAdapter();

    protected abstract List<?> initResultsList();

    protected abstract List<Integer> initResultTypesList();

    protected abstract int getSketchLogoResId();

    protected abstract int getLogoResId();

    protected abstract boolean isUserLoggedIn();

    protected abstract void removeUserDetails();

    protected abstract String getUserName();

    @Subscribe
    public void searchHashtag( SearchHashtagEvent event )
    {
        if ( TextUtils.isEmpty( getEnteredHashtag() ) )
        {
            return;
        }

        if ( !isUserLoggedIn() )
        {
            viewHolder.btnLogin.animate().rotationXBy( 360 ).setDuration( 1000 ).start();
            return;
        }
        timedSearchHandler.removeCallbacks( timedSearchRunner );
        // We use the listview tag to keep track of selected position. On a new search, we clear this tag.
        viewHolder.lvResultsList.setTag( null );
        viewHolder.srlReady.setRefreshing( false );
        viewHolder.sitesFooterView.showView( SitesFooterView.NORMAL );
        sitesSearchHandler.cancelCurrentSearch();
        sitesSearchHandler.beginSearch( SearchType.INITIAL, getEnteredHashtag() );
    }

    public void showView( int activeView )
    {
        int currentIndex = vaSitesView.indexOfChild( vaSitesView.getCurrentView() );
        if ( ( currentIndex == LOADING || currentIndex == READY ) && ( activeView == READY || activeView == LOADING ) )
        {
            vaSitesView.setInAnimation( fadeIn );
            vaSitesView.setOutAnimation( fadeOut );
        }
        vaSitesView.setDisplayedChild( activeView );
        vaSitesView.setInAnimation( null );
        vaSitesView.setOutAnimation( null );
        this.activeView = activeView;
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
        Toast.makeText( getActivity(), getResources().getString( getLoggedInToastTextId() ) + getUserName(), Toast.LENGTH_LONG ).show();
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

    private void doLoadNewerResults( String hashtag )
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( getActivity() );
            return;
        }
        if ( !isUserLoggedIn() )
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
        if ( !isUserLoggedIn() )
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
    public void whileSearching( int searchType )
    {
        switch ( searchType )
        {
            case SearchType.INITIAL:
                results.clear();
                resultTypes.clear();
                sitesListAdapter.notifyDataSetChanged();
                showView( LOADING );
                break;
            case SearchType.OLDER:
                viewHolder.sitesFooterView.showView( SitesFooterView.LOADING );
                break;
            case SearchType.NEWER:
                viewHolder.srlReady.setRefreshing( true );
                break;
            case SearchType.TIMED:
                break;
        }
    }

    @Override
    public void afterSearching( int searchType, List<?> searchResults )
    {
        switch ( searchType )
        {
            case SearchType.INITIAL:
                afterInitialSearch( searchResults );
                break;
            case SearchType.OLDER:
                afterOlderSearch( searchResults );
                break;
            case SearchType.NEWER:
                afterNewerSearch( searchResults );
                break;
            case SearchType.TIMED:
                afterTimedSearch( searchResults );
                break;
        }
    }

    public void afterInitialSearch( List<?> searchResults )
    {
        showView( READY );
        if ( !searchResults.isEmpty() )
        {
            updateResultsAndTypes( SearchType.INITIAL, searchResults );
            sitesListAdapter.notifyDataSetChanged();
        }
        else
        {
            viewHolder.sitesEmptyView.setText( String.format( "No results found for %s. Try again?", getEnteredHashtag() ) );
        }
        postNextTimedSearch();
    }

    public void afterOlderSearch( List<?> searchResults )
    {
        viewHolder.sitesFooterView.showView( SitesFooterView.NORMAL );
        if ( !searchResults.isEmpty() )
        {
            updateResultsAndTypes( SearchType.OLDER, searchResults );
            sitesListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_older_results ), Toast.LENGTH_LONG ).show();
        }
    }

    public void afterNewerSearch( List<?> searchResults )
    {
        viewHolder.srlReady.setRefreshing( false );
        if ( !searchResults.isEmpty() )
        {
            int newFirstVisiblePositionIndex = viewHolder.lvResultsList.getFirstVisiblePosition() + searchResults.size();
            int topOffset = null == viewHolder.lvResultsList.getChildAt( 0 ) ? 0 : viewHolder.lvResultsList.getChildAt( 0 ).getTop();
            updateResultsAndTypes( SearchType.NEWER, searchResults );
            sitesListAdapter.notifyDataSetChanged();
            viewHolder.lvResultsList.setSelectionFromTop( newFirstVisiblePositionIndex, topOffset );
            viewHolder.newResultsBar.setVisibility( View.VISIBLE );
            viewHolder.newResultsBar.setResultsCount( viewHolder.newResultsBar.getResultsCount() + searchResults.size() );
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_newer_results ), Toast.LENGTH_LONG ).show();
        }
    }

    public void afterTimedSearch( List<?> searchResults )
    {
        if ( !searchResults.isEmpty() )
        {
            int newFirstVisiblePositionIndex = viewHolder.lvResultsList.getFirstVisiblePosition() + searchResults.size();
            int topOffset = null == viewHolder.lvResultsList.getChildAt( 0 ) ? 0 : viewHolder.lvResultsList.getChildAt( 0 ).getTop();
            updateResultsAndTypes( SearchType.TIMED, searchResults );
            sitesListAdapter.notifyDataSetChanged();
            viewHolder.lvResultsList.setSelectionFromTop( newFirstVisiblePositionIndex, topOffset );
            viewHolder.newResultsBar.setVisibility( View.VISIBLE );
            viewHolder.newResultsBar.setResultsCount( viewHolder.newResultsBar.getResultsCount() + searchResults.size() );
        }
        postNextTimedSearch();
    }

    protected abstract void updateResultsAndTypes( int searchType, List<?> searchResults );

    @Override
    public void onError( int searchType )
    {
        switch ( searchType )
        {
            case SearchType.INITIAL:
                showView( READY );
                viewHolder.sitesEmptyView.setText( "Something went wrong :( Try again?" );
                break;
            case SearchType.OLDER:
                viewHolder.sitesFooterView.showView( SitesFooterView.ERROR );
                break;
            case SearchType.NEWER:
                viewHolder.srlReady.setRefreshing( false );
                Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_newer_results_error ), Toast.LENGTH_LONG ).show();
                break;
            case SearchType.TIMED:
                postNextTimedSearch();
                break;
        }
    }

    /*
    *********************** for UserHandlerListener ****************
     */
    public void logoutUser()
    {
        removeUserDetails();
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
                ( viewHolder.lvResultsList.getLastVisiblePosition() == viewHolder.lvResultsList.getPositionForView( view ) ) )
        {
            viewHolder.lvResultsList.smoothScrollBy( hiddenHeight, 2 * hiddenHeight );
        }
    }

    @Override
    public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id )
    {
        final Object result = parent.getItemAtPosition( position );
        PopupMenu popupMenu = new PopupMenu( view.getContext(), view );
        popupMenu.getMenuInflater().inflate( R.menu.sites_list_row_popup_menu, popupMenu.getMenu() );
        popupMenu.getMenu().findItem( R.id.it_open_in_browser ).setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick( MenuItem item )
            {
                Intent i = new Intent( Intent.ACTION_VIEW );
                i.setData( getResultUrl( result ) );
                getActivity().startActivity( i );
                return true;
            }
        } );
        popupMenu.getMenu().findItem( R.id.it_share ).setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick( MenuItem item )
            {
                Intent i = new Intent( Intent.ACTION_SEND );
                i.putExtra( Intent.EXTRA_TEXT, getResultText( result ) );
                i.setType( "text/plain" );
                getActivity().startActivity( i );
                return true;
            }
        } );
        popupMenu.show();
        return true;
    }

    protected abstract String getResultText( Object result );

    protected abstract Uri getResultUrl( Object result );

    protected static class ViewHolder
    {
        public ListView             lvResultsList;
        public SitesEmptyView       sitesEmptyView;
        public SitesFooterView      sitesFooterView;
        public MySwipeRefreshLayout srlReady;
        public NewResultsBar        newResultsBar;
        public ProgressBar          pgbrLoadingResults;
        public Button               btnLogin;
    }

    public int getActiveView()
    {
        return this.activeView;
    }
}
