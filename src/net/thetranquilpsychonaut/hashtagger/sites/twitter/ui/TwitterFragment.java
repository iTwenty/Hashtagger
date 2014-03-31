package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.Activity;
import android.content.Intent;
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
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.*;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFooter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, TwitterUserHandlerListener, TwitterSearchHandlerListener, TwitterFooterListener
{
    ArrayList<Status>    currentStatuses;
    TwitterListAdapter   twitterListAdapter;
    String               hashtag;
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
        twitterUserHandler.setListener( this );
        return twitterUserHandler;
    }

    @Override
    protected SitesSearchHandler getSitesSearchHandler()
    {
        twitterSearchHandler = new TwitterSearchHandler();
        twitterSearchHandler.setListener( this );
        return twitterSearchHandler;
    }

    @Override
    protected void onViewsCreated()
    {
        if ( !TwitterUserHandler.isUserLoggedIn() )
            showView( SitesView.LOGIN );
    }

    @Override
    protected View fetchView( SitesView sitesView, LayoutInflater inflater )
    {
        switch ( sitesView )
        {
            case READY:
                return getViewReady( inflater );
            case LOADING:
                return getViewLoading( inflater );
            case LOGIN:
                return getViewLogin( inflater );
            case ERROR:
                return getViewError( inflater );
        }
        return null;
    }

    private View getViewReady( LayoutInflater inflater )
    {
        View viewReady = inflater.inflate( R.layout.view_ready, null );
        readyHolder = new Ready();
        currentStatuses = new ArrayList<Status>();
        twitterListAdapter = new TwitterListAdapter( getActivity(), R.layout.fragment_twitter_list_row, currentStatuses );
        readyHolder.srlReady = ( SwipeRefreshLayout ) viewReady.findViewById( R.id.srl_ready );
        readyHolder.srlReady.setOnRefreshListener( this );
        readyHolder.srlReady.setColorScheme( android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light );
        readyHolder.lvResultsList = ( ListView ) viewReady.findViewById( R.id.lv_results_list );
        readyHolder.lvResultsList.setAdapter( twitterListAdapter );
        readyHolder.lvResultsListEmpty = ( TextView ) viewReady.findViewById( R.id.tv_results_list_empty );
        readyHolder.lvResultsList.setEmptyView( readyHolder.lvResultsListEmpty );
        return viewReady;
    }

    private View getViewLoading( LayoutInflater inflater )
    {
        View viewLoading = inflater.inflate( R.layout.view_loading, null );
        loadingHolder = new Loading();
        loadingHolder.pgbrLoadingResults = ( ProgressBar ) viewLoading.findViewById( R.id.pgbr_loading_results );
        return viewLoading;
    }

    private View getViewLogin( LayoutInflater inflater )
    {
        View viewLogin = inflater.inflate( R.layout.view_login, null );
        loginHolder = new Login();
        loginHolder.btnLogin = ( Button ) viewLogin.findViewById( R.id.btn_login );
        loginHolder.btnLogin.setOnClickListener( this );
        return viewLogin;
    }

    private View getViewError( LayoutInflater inflater )
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
        twitterFooter.setListener( this );
        twitterFooter.appendFooterToList( readyHolder.lvResultsList );
        readyHolder.lvResultsList.setAdapter( twitterListAdapter );
        return twitterFooter;
    }

    @Override
    public void searchHashtag( String hashtag )
    {
        if ( !TwitterUserHandler.isUserLoggedIn() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_twitter_not_logged_in ), Toast.LENGTH_LONG ).show();
            return;
        }
        this.hashtag = hashtag;
        getActivity().setTitle( hashtag );
        twitterSearchHandler.setHashtag( this.hashtag );
        twitterSearchHandler.beginSearch( SearchType.INITIAL );
    }

    private void doLogin()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_twitter_not_logged_in ), Toast.LENGTH_LONG ).show();
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
        twitterSearchHandler.beginSearch( SearchType.OLDER );
    }

    private void doLoadNewerResults()
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_toast_twitter_not_logged_in ), Toast.LENGTH_LONG ).show();
            return;
        }
        twitterSearchHandler.beginSearch( SearchType.NEWER );
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
            case R.id.it_refresh:
                Toast.makeText( getActivity(), "Refresh pressed", Toast.LENGTH_SHORT ).show();
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
    public void afterSearching( SearchType searchType, List<Status> statuses )
    {
        switch ( searchType )
        {
            case INITIAL:
                afterCurrentSearch( statuses );
                break;
            case OLDER:
                afterOlderSearch( statuses );
                break;
            case NEWER:
                afterNewerSearch( statuses );
                break;
        }
    }

    private void afterCurrentSearch( List<Status> statuses )
    {
        showView( SitesView.READY );
        twitterListAdapter.clear();
        if ( null != statuses )
        {
            twitterListAdapter.addAll( statuses );
        }
        else
        {
            readyHolder.lvResultsListEmpty.setText( getResources().getString( R.string.str_no_results ) );
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
            Toast.makeText( getActivity(), "No older results", Toast.LENGTH_LONG ).show();
        }

    }

    private void afterNewerSearch( List<Status> statuses )
    {
        readyHolder.srlReady.setRefreshing( false );
        if ( null != statuses )
        {
            currentStatuses.addAll( 0, statuses );
            twitterListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), "No newer results", Toast.LENGTH_LONG ).show();
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
