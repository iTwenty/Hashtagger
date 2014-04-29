package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.components.FacebookSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.components.FacebookUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/10/14.
 */
public class FacebookFragment extends SitesFragment
{
    @Override
    protected int getLogo()
    {
        return R.drawable.facebook_logo_monochrome;
    }

    @Override
    protected SitesUserHandler initSitesUserHandler()
    {
        FacebookUserHandler facebookUserHandler = new FacebookUserHandler( this );
        return facebookUserHandler;
    }

    @Override
    protected SitesSearchHandler initSitesSearchHandler()
    {
        FacebookSearchHandler facebookSearchHandler = new FacebookSearchHandler( this );
        return facebookSearchHandler;
    }

    @Override
    protected SitesListAdapter initSitesListAdapter()
    {
        FacebookListAdapter facebookListAdapter = new FacebookListAdapter( getActivity(), R.layout.twitter_list_row, results );
        return facebookListAdapter;
    }

    @Override
    protected List<?> initResultsList()
    {
        List<Post> results = new ArrayList<Post>();
        return results;
    }

    @Override
    protected int getPosition()
    {
        return HashtaggerApp.FACEBOOK_POSITION;
    }

    @Override
    protected int getNotLoggedInToastTextId()
    {
        return R.string.str_toast_facebook_not_logged_in;
    }

    @Override
    protected int getLoggedInToastTextId()
    {
        return R.string.str_toast_facebook_logged_in_as;
    }

    @Override
    protected int getLoginFailureToastTextId()
    {
        return R.string.str_toast_facebook_login_failed;
    }

    @Override
    protected int getLoginRequestCode()
    {
        return HashtaggerApp.FACEBOOK_LOGIN_REQUEST_CODE;
    }

    @Override
    protected Class<?> getLoginActivityClassName()
    {
        return FacebookLoginActivity.class;
    }

    @Override
    public void onUserLoggedIn()
    {
        ( ( FacebookSearchHandler ) sitesSearchHandler ).setAccessToken();
        super.onUserLoggedIn();
    }

    @Override
    public void onUserLoggedOut()
    {
        ( ( FacebookSearchHandler ) sitesSearchHandler ).clearAccessToken();
        super.onUserLoggedOut();
    }

    @Override
    protected void addToEnd( List<?> searchResults )
    {
        ( ( List<Post> ) results ).addAll( ( List<Post> ) searchResults );
    }

    @Override
    protected void addToStart( List<?> searchResults )
    {
        ( ( List<Post> ) results ).addAll( 0, ( List<Post> ) searchResults );
    }
}