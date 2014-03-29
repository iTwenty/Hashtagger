package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.exception.NoNetworkException;
import net.thetranquilpsychonaut.hashtagger.exception.NotLoggedInException;
import net.thetranquilpsychonaut.hashtagger.otto.HashtagEvent;
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
public class TwitterFragment extends SitesFragment implements View.OnClickListener
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
        twitterUserHandler.setListener( new TwitterUserHandlerListenerImpl() );
        return twitterUserHandler;
    }

    @Override
    protected SitesSearchHandler getSitesSearchHandler()
    {
        twitterSearchHandler = new TwitterSearchHandler();
        twitterSearchHandler.setListener( new TwitterSearchHandlerListenerImpl() );
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
        twitterFooter.setListener( new TwitterFooterListenerImpl() );
        twitterFooter.appendFooterToList( readyHolder.lvResultsList );
        readyHolder.lvResultsList.setAdapter( twitterListAdapter );
        return twitterFooter;
    }

    @Subscribe
    public void searchHashtag( HashtagEvent event )
    {
        try
        {
            ensureNetworkConnected();
            ensureUserLoggedIn();
        }
        catch ( NoNetworkException e )
        {
            Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
            return;
        }
        catch ( NotLoggedInException e )
        {
            Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
            return;
        }
        twitterListAdapter.clear();
        twitterListAdapter.notifyDataSetChanged();
        this.hashtag = event.getHashtag();
        getActivity().setTitle( hashtag );
        twitterSearchHandler.setHashtag( this.hashtag );
        twitterSearchHandler.beginSearch( SearchType.CURRENT );
    }

    @Override
    protected void ensureUserLoggedIn() throws NotLoggedInException
    {
        if ( !twitterUserHandler.isUserLoggedIn() )
            throw new NotLoggedInException( "Please sign in to Twitter first." );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( loginHolder.btnLogin ) )
            doLogin();
    }

    private void doLogin()
    {
        try
        {
            ensureNetworkConnected();
        }
        catch ( NoNetworkException e )
        {
            Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
            return;
        }
        Intent i = new Intent( getActivity(), TwitterAuthActivity.class );
        getActivity().startActivityForResult( i, HashtaggerApp.TWITTER_REQUEST_CODE );
    }

    private void dolLoadOlderResults()
    {
        try
        {
            ensureNetworkConnected();
        }
        catch ( NoNetworkException e )
        {
            Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
            return;
        }
        twitterSearchHandler.beginSearch( SearchType.OLDER );
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

    private void onUserLoggedOut()
    {
        twitterSearchHandler.clearAccessToken();
        twitterListAdapter.clear();
        twitterListAdapter.notifyDataSetChanged();
        getActivity().setTitle( getResources().getString( R.string.app_name ) );
        showView( SitesView.LOGIN );
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

    private class TwitterSearchHandlerListenerImpl implements TwitterSearchHandlerListener
    {
        @Override
        public void whileSearching( SearchType searchType )
        {
            switch ( searchType )
            {
                case CURRENT:
                    showView( SitesView.LOADING );
                    break;
                case OLDER:
                    twitterFooter.showFooterView( SitesFooter.SitesFooterView.LOADING );
                    break;
                case NEWER:
                    break;
            }
        }

        @Override
        public void afterSearching( SearchType searchType, List<Status> statuses )
        {
            switch ( searchType )
            {
                case CURRENT:
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
            if ( null != statuses )
            {
                twitterListAdapter.addAll( statuses );
                twitterListAdapter.notifyDataSetChanged();
            }
            else
            {
                readyHolder.lvResultsListEmpty.setText( getResources().getString( R.string.str_no_results ) );
            }
        }

        private void afterOlderSearch( List<Status> statuses )
        {
            twitterFooter.showFooterView( SitesFooter.SitesFooterView.LOAD_OLDER );
            if ( null != statuses )
            {
                twitterListAdapter.addAll( statuses );
                twitterListAdapter.notifyDataSetChanged();
            }

        }

        private void afterNewerSearch( List<Status> statuses )
        {
            if ( null != statuses )
            {
                currentStatuses.addAll( 0, statuses );
                twitterListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onError( SearchType searchType )
        {
            switch ( searchType )
            {
                case CURRENT:
                    showView( SitesView.ERROR );
                    break;
                case OLDER:
                    twitterFooter.showFooterView( SitesFooter.SitesFooterView.ERROR );
            }
        }
    }

    private class TwitterUserHandlerListenerImpl implements TwitterUserHandlerListener
    {
        @Override
        public void onUserLoggedOut()
        {
            TwitterFragment.this.onUserLoggedOut();
        }
    }

    private class TwitterFooterListenerImpl implements TwitterFooterListener
    {
        @Override
        public void onLoadOlderResultsClicked()
        {
            dolLoadOlderResults();
        }

        @Override
        public void onRetryClicked()
        {
        }
    }

    private static class Ready
    {
        public ListView lvResultsList;
        public TextView lvResultsListEmpty;
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
