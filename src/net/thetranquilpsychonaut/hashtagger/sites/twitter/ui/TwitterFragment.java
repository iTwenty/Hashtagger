package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.net.Uri;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragmentData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment
{
    public static SimplePageDescriptor descriptor = new SimplePageDescriptor( HashtaggerApp.TWITTER, HashtaggerApp.TWITTER );

    @Override
    protected int getLogo()
    {
        return R.drawable.twitter_logo;
    }

    @Override
    protected int getLoginButtonBackgroundId()
    {
        return R.drawable.selector_twitter_background;
    }

    @Override
    protected void saveData()
    {
        SitesFragmentData.Twitter.statuses = ( List<Status> ) results;
        SitesFragmentData.Twitter.statusTypes = resultTypes;
    }

    @Override
    protected String getLoginButtonText()
    {
        return getResources().getString( R.string.str_twitter_login_button_text );
    }

    @Override
    protected SitesUserHandler initSitesUserHandler()
    {
        return new TwitterUserHandler( this );
    }

    @Override
    protected SitesSearchHandler initSitesSearchHandler()
    {
        return new TwitterSearchHandler( this );
    }

    @Override
    protected SitesListAdapter initSitesListAdapter()
    {
        return new TwitterListAdapter( getActivity(), 0, results, resultTypes );
    }

    @Override
    protected List<?> initResultsList()
    {
        return null == SitesFragmentData.Twitter.statuses ? new ArrayList<Status>() : SitesFragmentData.Twitter.statuses;
    }

    @Override
    protected List<Integer> initResultTypesList()
    {
        return null == SitesFragmentData.Twitter.statusTypes ? new ArrayList<Integer>() : SitesFragmentData.Twitter.statusTypes;
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
        return HashtaggerApp.TWITTER_VALUE;
    }

    @Override
    protected Class<?> getLoginActivityClassName()
    {
        return TwitterLoginActivity.class;
    }

    @Override
    protected void addToEnd( List<?> searchResults )
    {
        ( ( List<Status> ) results ).addAll( ( List<Status> ) searchResults );
    }

    @Override
    protected void addToStart( List<?> searchResults )
    {
        ( ( List<Status> ) results ).addAll( 0, ( List<Status> ) searchResults );
    }

    @Override
    protected String getResultText( Object result )
    {
        Status status = ( Status ) result;
        return status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText();
    }

    @Override
    protected Uri getResultUrl( Object result )
    {
        return Helper.getTwitterStatusUrl( ( Status ) result );
    }

    @Override
    public void onResume()
    {
        super.onResume();
        HashtaggerApp.bus.register( this );
    }

    @Override
    public void onPause()
    {
        HashtaggerApp.bus.unregister( this );
        super.onPause();
    }

    @Subscribe
    public void onRetweetDone( TwitterRetweetEvent event )
    {
        if ( event.getSuccess() )
        {
            ( ( List<Status> ) results ).set( event.getPosition(), event.getStatus() );
            sitesListAdapter.notifyDataSetChanged();
            Toast.makeText( getActivity(), "Retweeted like a champ!", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( getActivity(), "Failed to retweet", Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void onFavoriteDone( TwitterFavoriteEvent event )
    {
        if ( event.getSuccess() )
        {
            ( ( List<Status> ) results ).set( event.getPosition(), event.getStatus() );
            sitesListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), "Failed to favorite", Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void onReplyDone( TwitterReplyEvent event )
    {
        if ( event.getSuccess() )
        {
            Toast.makeText( getActivity(), "Replied like a champ!", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( getActivity(), "Failed to reply", Toast.LENGTH_SHORT ).show();
        }
    }
}
