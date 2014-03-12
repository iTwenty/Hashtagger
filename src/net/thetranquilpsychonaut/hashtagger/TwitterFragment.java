package net.thetranquilpsychonaut.hashtagger;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import twitter4j.*;

import java.util.ArrayList;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment
{
    ArrayList<Status>  allStatuses;
    ArrayList<Status>  newStatuses;
    TwitterListAdapter twitterListAdapter;
    String             hashtag;
    Ready              readyHolder;
    Loading            loadingHolder;
    NoNetwork          noNetworkHolder;
    TwitterTask        twitterTask;

    @Override
    protected View getViewReady( LayoutInflater inflater )
    {
        View viewReady = inflater.inflate( R.layout.view_ready, null );
        readyHolder = new Ready();
        allStatuses = new ArrayList<Status>();
        newStatuses = new ArrayList<Status>();
        twitterListAdapter = new TwitterListAdapter( HashtaggerApp.app, R.layout.fragment_twitter_list_row, allStatuses );
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

    @Subscribe
    public void searchHashtag( final String hashtag )
    {
        twitterListAdapter.clear();
        twitterListAdapter.notifyDataSetChanged();
        if ( null != twitterTask )
            twitterTask = null;
        if ( null == this.hashtag )
            this.hashtag = hashtag;
        if ( !HashtaggerApp.isNetworkConnected() )
            return;
        twitterTask = new TwitterTask( this );
        twitterTask.execute( hashtag );
    }

    @Override
    public void onConnected()
    {
        super.onConnected();
    }

    @Override
    public void onDisconnected()
    {
        super.onDisconnected();
    }

    class Ready
    {
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

    static class TwitterTask extends AsyncTask<String, Void, QueryResult>
    {
        Twitter         twitter;
        Query           query;
        QueryResult     result;
        TwitterFragment fragment;

        public TwitterTask( TwitterFragment f )
        {
            twitter = new TwitterFactory( HashtaggerApp.getTwitterConfiguration() ).getInstance();
            query = new Query();
            result = null;
            fragment = f;
        }

        @Override
        protected void onPreExecute()
        {
            fragment.showLoadingView();
        }

        @Override
        protected QueryResult doInBackground( String... params )
        {

            query.setQuery( params[0] );
            try
            {
                result = twitter.search( query );
            }
            catch ( TwitterException e )
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( QueryResult queryResult )
        {
            fragment.showReadyView();
            fragment.twitterListAdapter.addAll( queryResult.getTweets() );
            fragment.twitterListAdapter.notifyDataSetChanged();
        }
    }

}
