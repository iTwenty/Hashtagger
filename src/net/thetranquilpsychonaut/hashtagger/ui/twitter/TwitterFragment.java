package net.thetranquilpsychonaut.hashtagger.ui.twitter;

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
import net.thetranquilpsychonaut.hashtagger.exception.NoNetworkException;
import net.thetranquilpsychonaut.hashtagger.exception.NotLoggedInException;
import net.thetranquilpsychonaut.hashtagger.otto.HashtagEvent;
import net.thetranquilpsychonaut.hashtagger.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.ui.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.ui.SitesUserHandler;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment implements View.OnClickListener
{
    ArrayList<Status>    currentStatuses;
    ArrayList<Status>    newStatuses;
    ArrayList<Status>    oldStatuses;
    TwitterListAdapter   twitterListAdapter;
    String               hashtag;
    Ready                readyHolder;
    Loading              loadingHolder;
    Login                loginHolder;
    Error                errorHolder;
    Footer               footerHolder;
    TwitterSearchHandler twitterSearchHandler;
    TwitterUserHandler   twitterUserHandler;

    private TwitterSearchHandlerListenerImpl twitterSearchHandlerListener;
    private TwitterUserHandlerListenerImpl   twitteryUserHandlerListener;

    @Override
    protected SitesUserHandler getSitesUserHandler()
    {
        twitterUserHandler = new TwitterUserHandler();
        twitteryUserHandlerListener = new TwitterUserHandlerListenerImpl();
        twitterUserHandler.setListener( twitteryUserHandlerListener );
        return twitterUserHandler;
    }

    @Override
    protected SitesSearchHandler getSitesSearchHandler()
    {
        twitterSearchHandler = new TwitterSearchHandler();
        twitterSearchHandlerListener = new TwitterSearchHandlerListenerImpl();
        twitterSearchHandler.setListener( twitterSearchHandlerListener );
        return twitterSearchHandler;
    }

    @Override
    protected void onViewsCreated()
    {
        if ( !TwitterUserHandler.isUserLoggedIn() )
            showView( viewLogin );
    }

    @Override
    protected View fetchView( Views views, LayoutInflater inflater )
    {
        switch ( views )
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
        footerHolder = getFooterHolder( inflater );
        currentStatuses = new ArrayList<Status>();
        newStatuses = new ArrayList<Status>();
        oldStatuses = new ArrayList<Status>();
        twitterListAdapter = new TwitterListAdapter( getActivity(), R.layout.fragment_twitter_list_row, currentStatuses );
        readyHolder.lvResultsList = ( ListView ) viewReady.findViewById( R.id.lv_results_list );
        readyHolder.lvResultsList.addFooterView( footerHolder.vaFooterView, readyHolder.lvResultsList, true );
        readyHolder.lvResultsList.setAdapter( twitterListAdapter );
        readyHolder.lvResultsListEmpty = ( TextView ) viewReady.findViewById( R.id.tv_results_list_empty );
        readyHolder.lvResultsList.setEmptyView( readyHolder.lvResultsListEmpty );
        return viewReady;
    }

    private Footer getFooterHolder(LayoutInflater inflater)
    {
        Footer footerHolder = new Footer();
        footerHolder.vaFooterView = ( ViewAnimator )inflater.inflate( R.layout.footer_view_results_list, null );
        footerHolder.btnFooterLoadOlderResults = ( Button )footerHolder.vaFooterView.findViewById( R.id.btn_footer_load_older_results );
        footerHolder.btnFooterLoadOlderResults.setOnClickListener( this );
        footerHolder.pgbrFooterLoading = ( ProgressBar )footerHolder.vaFooterView.findViewById( R.id.pgbr_footer_loading );
        footerHolder.tvFooterNoNetworkMsg = ( TextView )footerHolder.vaFooterView.findViewById( R.id.tv_footer_no_network_msg );
        footerHolder.btnFooterRetry = ( Button )footerHolder.vaFooterView.findViewById( R.id.btn_footer_retry );
        footerHolder.btnFooterRetry.setOnClickListener( this );
        return footerHolder;
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
        newStatuses.clear();
        twitterListAdapter.clear();
        twitterListAdapter.notifyDataSetChanged();
        this.hashtag = event.getHashtag();
        getActivity().setTitle( hashtag );
        if ( null != twitterSearchHandler )
            twitterSearchHandler.destroyCurrentSearch();
        twitterSearchHandler.setHashtag( this.hashtag );
        twitterSearchHandler.beginSearch();
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
        else if( v.equals( footerHolder.btnFooterLoadOlderResults ) )
            dolLoadOlderResults();
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
        Toast.makeText( getActivity(), "pressed", Toast.LENGTH_LONG ).show();
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
        showView( viewReady );
        Toast.makeText( getActivity(), "Logged in to Twitter as " + twitterUserHandler.getUserName(), Toast.LENGTH_LONG ).show();
        getActivity().invalidateOptionsMenu();
    }

    private void onUserLoggedOut()
    {
        twitterSearchHandler.destroyCurrentSearch();
        twitterSearchHandler.clearAccessToken();
        newStatuses.clear();
        twitterListAdapter.clear();
        twitterListAdapter.notifyDataSetChanged();
        twitterSearchHandler.destroyCurrentSearch();
        getActivity().setTitle( getResources().getString( R.string.app_name ) );
        showView( viewLogin );
        getActivity().invalidateOptionsMenu();
    }

    private void onLoginFailure()
    {
        Toast.makeText( getActivity(), "Could not log you in. Please try again.", Toast.LENGTH_LONG ).show();
        showView( viewLogin );
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
        public void whileSearching()
        {
            showView( viewLoading );
        }

        @Override
        public void afterSearching( List<Status> statuses )
        {
            showView( viewReady );
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

        @Override
        public void onError()
        {
            showView( viewError );
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

    private static class Ready
    {
        public ListView       lvResultsList;
        public TextView       lvResultsListEmpty;
    }

    private static class Footer
    {
        public ViewAnimator vaFooterView;
        public Button       btnFooterLoadOlderResults;
        public ProgressBar  pgbrFooterLoading;
        public TextView     tvFooterNoNetworkMsg;
        public Button       btnFooterRetry;
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
        twitterSearchHandler.destroyCurrentSearch();
        super.onDestroy();
    }
}
