package net.thetranquilpsychonaut.hashtagger;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment
{
    ArrayList<Status>        allStatuses;
    ArrayList<Status>        newStatuses;
    TwitterListAdapter       twitterListAdapter;
    ConfigurationBuilder     cb;
    String                   hashtag;

    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        twitterListAdapter = new TwitterListAdapter( getActivity(), R.layout.fragment_twitter_list_row, allStatuses );
        hashtag = null;
        cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey( HashtaggerApp.OAUTH_CONSUMER_KEY );
        cb.setOAuthConsumerSecret( HashtaggerApp.OAUTH_CONSUMER_SECRET );
        cb.setOAuthAccessToken( HashtaggerApp.OAUTH_ACCESS_TOKEN );
        cb.setOAuthAccessTokenSecret( HashtaggerApp.OAUTH_ACCESS_TOKEN_SECRET );
        super.onActivityCreated( savedInstanceState );
    }

    @Override
    protected View getViewReady( LayoutInflater inflater )
    {
        viewReady = inflater.inflate( R.layout.view_ready, null );
        return viewReady;
    }

    @Override
    protected View getViewLoading( LayoutInflater inflater )
    {
        viewLoading = inflater.inflate( R.layout.view_loading, null );
        return viewLoading;
    }

    @Override
    protected View getViewNoNetwork( LayoutInflater inflater )
    {
        viewNoNetwork = inflater.inflate( R.layout.view_no_network, null );
        return viewNoNetwork;
    }

    @Override
    protected View getViewLogin( LayoutInflater inflater )
    {
        viewLogin = inflater.inflate( R.layout.view_login, null );
        return viewLogin;
    }

    @Subscribe
    public void searchHashtag( String hashtag )
    {
        if ( null == this.hashtag )
            this.hashtag = "#" + hashtag;
        Toast.makeText( getActivity(), this.hashtag, Toast.LENGTH_SHORT ).show();
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
}
