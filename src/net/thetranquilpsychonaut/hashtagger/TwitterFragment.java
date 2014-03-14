package net.thetranquilpsychonaut.hashtagger;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment implements TwitterHandlerListener, View.OnClickListener
{
    ArrayList<Status>  allStatuses;
    ArrayList<Status>  newStatuses;
    TwitterListAdapter twitterListAdapter;
    String             hashtag;
    Ready              readyHolder;
    Loading            loadingHolder;
    NoNetwork          noNetworkHolder;
    Error              errorHolder;
    Twitterhandler     twitterhandler;
    ColorStateList     tvListeningIndicatorColors;

    @Override
    protected SitesHandler getSitesHandler()
    {
        twitterhandler = new Twitterhandler();
        return twitterhandler;
    }

    @Override
    protected View getViewReady( LayoutInflater inflater )
    {
        View viewReady = inflater.inflate( R.layout.view_ready, null );
        readyHolder = new Ready();
        allStatuses = new ArrayList<Status>();
        newStatuses = new ArrayList<Status>();
        twitterListAdapter = new TwitterListAdapter( HashtaggerApp.app, R.layout.fragment_twitter_list_row, allStatuses );
        readyHolder.tvListeningIndicator = ( TextView )viewReady.findViewById( R.id.tv_listening_indicator );
        tvListeningIndicatorColors = readyHolder.tvListeningIndicator.getTextColors();
        readyHolder.lvResultsList = ( ListView ) viewReady.findViewById( R.id.lv_results_list );
        readyHolder.lvResultsList.setAdapter( twitterListAdapter );
        readyHolder.lvResultsListEmpty = ( TextView ) viewReady.findViewById( R.id.tv_results_list_empty );
        readyHolder.lvResultsList.setEmptyView( readyHolder.lvResultsListEmpty );
        readyHolder.btnNewResults = ( Button ) viewReady.findViewById( R.id.btn_new_results );
        readyHolder.btnNewResults.setVisibility( View.GONE );
        return viewReady;
    }

    @Override
    protected View getViewLoading( LayoutInflater inflater )
    {
        View viewLoading = inflater.inflate( R.layout.view_loading, null );
        loadingHolder = new Loading();
        loadingHolder.pgbrLoadingResults = ( ProgressBar ) viewLoading.findViewById( R.id.pgbr_loading_results );
        return viewLoading;
    }

    @Override
    protected View getViewNoNetwork( LayoutInflater inflater )
    {
        View viewNoNetwork = inflater.inflate( R.layout.view_no_network, null );
        noNetworkHolder = new NoNetwork();
        noNetworkHolder.tvNoNetwork = ( TextView ) viewNoNetwork.findViewById( R.id.tv_no_network );
        return viewNoNetwork;
    }

    @Override
    protected View getViewLogin( LayoutInflater inflater )
    {
        View viewLogin = inflater.inflate( R.layout.view_login, null );
        return viewLogin;
    }

    @Override
    protected View getViewError( LayoutInflater inflater )
    {
        View viewError = inflater.inflate( R.layout.view_error, null );
        errorHolder = new Error();
        errorHolder.tvError = ( TextView )viewError.findViewById( R.id.tv_error );
        return viewError;
    }

    @Subscribe
    public void searchHashtag( String hashtag )
    {
        newStatuses.clear();
        twitterListAdapter.clear();
        twitterListAdapter.notifyDataSetChanged();
        this.hashtag = hashtag;
        if( null != twitterhandler )
            twitterhandler.destroyCurrentSearch();
        twitterhandler.setHashtag( this.hashtag );
        twitterhandler.setListener( this );
        twitterhandler.beginSearch();
    }

    @Override
    public void onPreExecute()
    {
        showLoadingView();
    }

    @Override
    public void onPostExecute( List<Status> statuses )
    {
        showReadyView();
        if( null != statuses )
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
    public void onSwitchToListener()
    {
        readyHolder.tvListeningIndicator.setVisibility( View.VISIBLE );
        readyHolder.btnNewResults.setVisibility( View.VISIBLE );
        readyHolder.btnNewResults.setOnClickListener( this );
        updateButtonCount( readyHolder.btnNewResults, 0 );
    }

    @Override
    public void onStatus( Status status )
    {
        newStatuses.add( status );
        getActivity().runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                updateButtonCount( readyHolder.btnNewResults, newStatuses.size() );
            }
        } );
    }

    @Override
    public void onError()
    {
        showErrorView();
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( readyHolder.btnNewResults ) )
        {
            readyHolder.lvResultsList.smoothScrollToPosition( allStatuses.size() - 1 );
            twitterListAdapter.addAll( newStatuses );
            twitterListAdapter.notifyDataSetChanged();
            newStatuses.clear();
            updateButtonCount( ( Button ) v, newStatuses.size() );
        }
    }

    private void updateButtonCount( Button button, int size )
    {
        int resultStringResourceId = size == 1 ? R.string.str_new_result : R.string.str_new_results;
        button.setText( size + " " + getResources().getString( resultStringResourceId ) );
    }

    @Override
    public void onConnected()
    {
        if( twitterhandler.isInListeningMode() )
        {
            readyHolder.tvListeningIndicator.setBackgroundColor( getResources().getColor( android.R.color.transparent ) );
            readyHolder.tvListeningIndicator.setTextColor( tvListeningIndicatorColors.getDefaultColor() );
            readyHolder.tvListeningIndicator.setText( getResources().getString( R.string.str_listening_new_tweets ) );
        }
        super.onConnected();
    }

    @Override
    public void onDisconnected()
    {
        if( twitterhandler.isInListeningMode() )
        {
            readyHolder.tvListeningIndicator.setBackgroundColor( getResources().getColor( android.R.color.holo_red_light ) );
            readyHolder.tvListeningIndicator.setTextColor( getResources().getColor( android.R.color.black ) );
            readyHolder.tvListeningIndicator.setText( getResources().getString( R.string.str_no_network ) );
        }
        super.onDisconnected();
    }

    class Ready
    {
        public TextView tvListeningIndicator;
        public ListView lvResultsList;
        public TextView lvResultsListEmpty;
        public Button   btnNewResults;
    }

    ;

    class Loading
    {
        public ProgressBar pgbrLoadingResults;
    }

    ;

    class NoNetwork
    {
        public TextView tvNoNetwork;
    }

    ;

    class Error
    {
        public TextView tvError;
    }
}
