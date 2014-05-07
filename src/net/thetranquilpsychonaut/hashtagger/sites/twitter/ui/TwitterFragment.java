package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
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
    protected String getLoginButtonText()
    {
        return getResources().getString( R.string.str_twitter_login_button_text );
    }

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
        TwitterListAdapter twitterListAdapter = new TwitterListAdapter( getActivity(), 0, results );
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
}
