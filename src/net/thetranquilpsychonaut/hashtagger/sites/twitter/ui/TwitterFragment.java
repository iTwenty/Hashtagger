package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ExpandableStatus;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment
{
    @Override
    protected SitesUserHandler initSitesUserHandler()
    {
        TwitterUserHandler twitterUserHandler = new TwitterUserHandler( this );
        return twitterUserHandler;
    }

    @Override
    protected SitesSearchHandler initSitesSearchHandler()
    {
        TwitterSearchHandler twitterSearchHandler = new TwitterSearchHandler( this );
        return twitterSearchHandler;
    }

    @Override
    protected SitesListAdapter initSitesListAdapter()
    {
        TwitterListAdapter twitterListAdapter = new TwitterListAdapter( getActivity(), R.layout.fragment_twitter_list_row, ( ArrayList<ExpandableStatus> ) results );
        return twitterListAdapter;
    }

    @Override
    protected List<?> initResultsList()
    {
        List<ExpandableStatus> results = new ArrayList<ExpandableStatus>();
        return results;
    }

    @Override
    protected int getNotLoggedInToastTextId()
    {
        return R.string.str_toast_twitter_not_logged_in;
    }

    @Override
    protected int getLoggedInToastTextId()
    {
        return R.string.str_toast_twitter_logged_in_as;
    }

    @Override
    protected int getLoginFailureToastTextId()
    {
        return R.string.str_toast_twitter_login_failed;
    }

    @Override
    protected int getLoginRequestCode()
    {
        return HashtaggerApp.TWITTER_LOGIN_REQUEST_CODE;
    }

    @Override
    public void onUserLoggedIn()
    {
        ( ( TwitterSearchHandler ) sitesSearchHandler ).setAccessToken();
        super.onUserLoggedIn();
    }

    @Override
    public void onUserLoggedOut()
    {
        ( ( TwitterSearchHandler ) sitesSearchHandler ).clearAccessToken();
        super.onUserLoggedOut();
    }

    @Override
    protected Class<?> getLoginActivityClassName()
    {
        return TwitterLoginActivity.class;
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        ExpandableStatus es = ( ExpandableStatus ) parent.getItemAtPosition( position );
        TwitterListRow twitterListRow = ( TwitterListRow ) view.getTag();
        if ( es.isExpanded() )
        {
            twitterListRow.collapseRow();
            es.setExpanded( false );
        }
        else
        {
            twitterListRow.expandRow( es );
            es.setExpanded( true );
        }
    }

    @Override
    public void onDestroy()
    {
        sitesSearchHandler.unregisterReceiver();
        super.onDestroy();
    }

    @Override
    protected void addToEnd( List<?> searchResults )
    {
        for( Status status : ( ArrayList<Status> ) searchResults )
        {
            ( ( ArrayList<ExpandableStatus> ) results ).add( new ExpandableStatus( status, false ) );
        }
    }

    @Override
    protected void addToStart( List<?> searchResults )
    {
        Collections.reverse( searchResults );
        for( Status status : ( ArrayList<Status> ) searchResults )
        {
            ( ( ArrayList<ExpandableStatus> ) results ).add( 0, new ExpandableStatus( status, false ) );
        }
    }
}
