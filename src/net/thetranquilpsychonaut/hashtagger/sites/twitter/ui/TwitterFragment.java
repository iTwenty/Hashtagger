package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFooter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, SitesUserHandler.SitesUserListener, SitesSearchHandler.SitesSearchListener, SitesFooter.SitesFooterListener, AdapterView.OnItemClickListener
{
    private static final String TWITTER_RESULTS_LIST_KEY = HashtaggerApp.NAMESPACE + "twitter_results_list_key";
    private static final String    SRL_IS_REFRESHING_KEY = HashtaggerApp.NAMESPACE + "srl_is_refreshing_key";

    ArrayList<Status>    statuses;
    TwitterListAdapter   twitterListAdapter;
    Ready                readyHolder;
    Loading              loadingHolder;
    Login                loginHolder;
    Error                errorHolder;
    TwitterFooter        twitterFooter;
    TwitterSearchHandler twitterSearchHandler;
    TwitterUserHandler   twitterUserHandler;

    @Override
    protected SitesUserHandler getSitesUserHandler()
    {
        twitterUserHandler = new TwitterUserHandler();
        twitterUserHandler.setSitesUserListener( this );
        return twitterUserHandler;
    }

    @Override
    protected SitesSearchHandler getSitesSearchHandler()
    {
        twitterSearchHandler = new TwitterSearchHandler();
        twitterSearchHandler.setSitesSearchListener( this );
        return twitterSearchHandler;
    }

    @Override
    protected void onViewsCreated()
    {
        if ( !TwitterUserHandler.isUserLoggedIn() )
            showView( SitesView.LOGIN );
    }

    @Override
    protected View fetchView( SitesView sitesView, LayoutInflater inflater, Bundle savedInstaceState )
    {
        switch ( sitesView )
        {
            case READY:
                return getViewReady( inflater, savedInstaceState );
            case LOADING:
                return getViewLoading( inflater, savedInstaceState );
            case LOGIN:
                return getViewLogin( inflater, savedInstaceState );
            case ERROR:
                return getViewError( inflater, savedInstaceState );
        }
        return null;
    }

    private View getViewReady( LayoutInflater inflater, Bundle savedInstanceState )
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
            statuses = ( ArrayList<Status> ) savedInstanceState.getSerializable( TWITTER_RESULTS_LIST_KEY );
            readyHolder.srlReady.setRefreshing( savedInstanceState.getBoolean( SRL_IS_REFRESHING_KEY ) );
        }
        else
        {
            statuses = new ArrayList<Status>();
        }
        twitterListAdapter = new TwitterListAdapter( getActivity(), R.layout.fragment_twitter_list_row, statuses );
        readyHolder.lvResultsList = ( ListView ) viewReady.findViewById( R.id.lv_results_list );
        readyHolder.lvResultsList.setAdapter( twitterListAdapter );
        readyHolder.lvResultsList.setOnItemClickListener( this );
        readyHolder.lvResultsListEmpty = ( TextView ) viewReady.findViewById( R.id.tv_results_list_empty );
        readyHolder.lvResultsList.setEmptyView( readyHolder.lvResultsListEmpty );
        return viewReady;
    }

    private View getViewLoading( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewLoading = inflater.inflate( R.layout.view_loading, null );
        loadingHolder = new Loading();
        loadingHolder.pgbrLoadingResults = ( ProgressBar ) viewLoading.findViewById( R.id.pgbr_loading_results );
        return viewLoading;
    }

    private View getViewLogin( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewLogin = inflater.inflate( R.layout.view_login, null );
        loginHolder = new Login();
        loginHolder.btnLogin = ( Button ) viewLogin.findViewById( R.id.btn_login );
        loginHolder.btnLogin.setOnClickListener( this );
        return viewLogin;
    }

    private View getViewError( LayoutInflater inflater, Bundle savedInstanceState )
    {
        View viewError = inflater.inflate( R.layout.view_error, null );
        errorHolder = new Error();
        errorHolder.tvError = ( TextView ) viewError.findViewById( R.id.tv_error );
        return viewError;
    }

    @Override
    public SitesFooter getSitesFooter( LayoutInflater inflater )
    {
        twitterFooter = new TwitterFooter( inflater );
        twitterFooter.setSitesFooterListener( this );
        twitterFooter.appendFooterToList( readyHolder.lvResultsList );
        readyHolder.lvResultsList.setAdapter( twitterListAdapter );
        return twitterFooter;
    }

    @Override
    public void searchHashtag( String hashtag )
    {
        if ( !TwitterUserHandler.isUserLoggedIn() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_twitter_not_logged_in ), Toast.LENGTH_LONG ).show();
            return;
        }
        twitterSearchHandler.beginSearch( SearchType.INITIAL, hashtag );
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putSerializable( TWITTER_RESULTS_LIST_KEY, statuses );
        outState.putBoolean( SRL_IS_REFRESHING_KEY, readyHolder.srlReady.isRefreshing() );
    }

    private void doLogin()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        Intent i = new Intent( getActivity(), TwitterAuthActivity.class );
        getActivity().startActivityForResult( i, HashtaggerApp.TWITTER_REQUEST_CODE );
    }

    private void dolLoadOlderResults()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_twitter_not_logged_in ), Toast.LENGTH_LONG ).show();
            return;
        }
        twitterSearchHandler.beginSearch( SearchType.OLDER, ( ( SitesActivity ) getActivity() ).getHashtag() );
    }

    private void doLoadNewerResults()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        twitterSearchHandler.beginSearch( SearchType.NEWER, ( ( SitesActivity ) getActivity() ).getHashtag() );
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        Helper.debug( "onActivityResult" );
        if ( requestCode == HashtaggerApp.TWITTER_REQUEST_CODE )
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

    private void onUserLoggedIn()
    {
        twitterSearchHandler.setAccessToken();
        showView( SitesView.READY );
        Toast.makeText( getActivity(), "Logged in to Twitter as " + twitterUserHandler.getUserName(), Toast.LENGTH_LONG ).show();
        getActivity().invalidateOptionsMenu();
    }

    private void onLoginFailure()
    {
        Toast.makeText( getActivity(), "Could not log you in. Please try again.", Toast.LENGTH_LONG ).show();
        showView( SitesView.LOGIN );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch ( item.getItemId() )
        {
            case R.id.sv_hashtag:
                return false;
            case R.id.it_logout_twitter:
                twitterUserHandler.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public void onPrepareOptionsMenu( Menu menu )
    {
        super.onPrepareOptionsMenu( menu );
        if ( twitterUserHandler.isUserLoggedIn() )
            menu.findItem( R.id.it_logout_twitter ).setVisible( true );
        else
            menu.findItem( R.id.it_logout_twitter ).setVisible( false );
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

    /**
     * *************** For UserHandlerListener ****************
     */

    @Override
    public void onUserLoggedOut()
    {
        twitterSearchHandler.clearAccessToken();
        twitterListAdapter.clear();
        twitterListAdapter.notifyDataSetChanged();
        getActivity().setTitle( getResources().getString( R.string.app_name ) );
        showView( SitesView.LOGIN );
        getActivity().invalidateOptionsMenu();
    }


    /**
     * **************** for onRefreshListener **************
     */

    @Override
    public void onRefresh()
    {
        doLoadNewerResults();
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
                getActivity().invalidateOptionsMenu();
                break;
            case OLDER:
                twitterFooter.showFooterView( SitesFooter.SitesFooterView.LOADING );
                break;
            case NEWER:
                readyHolder.srlReady.setRefreshing( true );
                break;
        }
    }

    @Override
    public void afterSearching( SearchType searchType, Bundle resultBundle )
    {
        ArrayList<Status> results = ( ArrayList<Status> ) resultBundle.getSerializable( HashtaggerApp.TWITTER_SEARCH_RESULT_LIST_KEY );
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

    private void afterCurrentSearch( List<Status> statuses )
    {
        showView( SitesView.READY );
        twitterListAdapter.clear();
        if ( !statuses.isEmpty() )
        {
            twitterListAdapter.addAll( statuses );
        }
        else
        {
            readyHolder.lvResultsListEmpty.setText( getResources().getString( R.string.str_toast_no_results ) );
        }
        twitterListAdapter.notifyDataSetChanged();
    }

    private void afterOlderSearch( List<Status> statuses )
    {
        twitterFooter.showFooterView( SitesFooter.SitesFooterView.LOAD_OLDER );
        if ( null != statuses )
        {
            twitterListAdapter.addAll( statuses );
            twitterListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_older_results ), Toast.LENGTH_LONG ).show();
        }

    }

    private void afterNewerSearch( List<Status> statuses )
    {
        readyHolder.srlReady.setRefreshing( false );
        if ( null != statuses )
        {
            this.statuses.addAll( 0, statuses );
            twitterListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_newer_results ), Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void onError( SearchType searchType )
    {
        switch ( searchType )
        {
            case INITIAL:
                showView( SitesView.ERROR );
                break;
            case OLDER:
                twitterFooter.showFooterView( SitesFooter.SitesFooterView.ERROR );
                break;
            case NEWER:
                readyHolder.srlReady.setRefreshing( false );
                Toast.makeText( getActivity(), "Failed to load newer results", Toast.LENGTH_LONG ).show();
                break;
        }
    }

    /**
     * ************** for FooterListener **********************
     */

    @Override
    public void onLoadOlderResultsClicked()
    {
        dolLoadOlderResults();
    }

    @Override
    public void onRetryClicked()
    {
        dolLoadOlderResults();
    }

    /*
    *******************for listview onItemClickListener
     */
    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        Status status = ( Status ) parent.getItemAtPosition( position );
        TwitterDetailsDialogFragment twitterDetailsDialogFragment = TwitterDetailsDialogFragment.getInstance( status );
        twitterDetailsDialogFragment.show( getFragmentManager(), HashtaggerApp.TWITTER_DIALOG_TAG );
    }

    private static class Ready
    {
        public ListView lvResultsList;
        public TextView lvResultsListEmpty;
        SwipeRefreshLayout srlReady;
    }

    private static class Loading
    {
        public ProgressBar pgbrLoadingResults;
    }

    private static class Login
    {
        public Button btnLogin;
    }

    private static class Error
    {
        public TextView tvError;
    }

    @Override
    public void onDestroy()
    {
        twitterSearchHandler.unregisterReceiver();
        super.onDestroy();
    }
}
