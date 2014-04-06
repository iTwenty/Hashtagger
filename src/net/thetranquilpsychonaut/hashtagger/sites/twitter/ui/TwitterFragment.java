package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment implements AdapterView.OnItemClickListener
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
    protected ArrayAdapter<?> initResultsAdapter()
    {
        TwitterListAdapter twitterListAdapter = new TwitterListAdapter( getActivity(), R.layout.fragment_twitter_list_row, ( ArrayList<Status> ) results );
        return twitterListAdapter;
    }

    @Override
    protected List<?> initResultsList()
    {
        List<Status> results = new ArrayList<Status>();
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
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch ( item.getItemId() )
        {
            case R.id.sv_hashtag:
                return false;
            case R.id.it_logout_twitter:
                sitesUserHandler.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public void onPrepareOptionsMenu( Menu menu )
    {
        super.onPrepareOptionsMenu( menu );
        if ( sitesUserHandler.isUserLoggedIn() )
            menu.findItem( R.id.it_logout_twitter ).setVisible( true );
        else
            menu.findItem( R.id.it_logout_twitter ).setVisible( false );
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
        Status status = ( Status ) parent.getItemAtPosition( position );
        TwitterDetailsDialogFragment twitterDetailsDialogFragment = TwitterDetailsDialogFragment.getInstance( status );
        twitterDetailsDialogFragment.show( getFragmentManager(), HashtaggerApp.TWITTER_DIALOG_TAG );
    }

    @Override
    public void onDestroy()
    {
        sitesSearchHandler.unregisterReceiver();
        super.onDestroy();
    }

    @Override
    protected void addToEnd( List<?> statuses )
    {
        ( ( ArrayList<Status> ) results ).addAll( ( ArrayList<Status> ) statuses );
    }

    @Override
    protected void addToStart( List<?> statuses )
    {
        ( ( ArrayList<Status> ) results ).addAll( 0, ( ArrayList<Status> ) statuses );
    }
}
