package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.components.FacebookUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;

import java.util.List;

/**
 * Created by itwenty on 2/10/14.
 */
public class FacebookFragment extends SitesFragment
{
    @Override
    protected SitesUserHandler initSitesUserHandler()
    {
        FacebookUserHandler facebookUserHandler = new FacebookUserHandler( this );
        return facebookUserHandler;
    }

    @Override
    protected SitesSearchHandler initSitesSearchHandler()
    {
        return null;
    }

    @Override
    protected ArrayAdapter<?> initResultsAdapter()
    {
        return null;
    }

    @Override
    protected List<?> initResultsList()
    {
        return null;
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
    protected void addToEnd( List<?> statuses )
    {

    }

    @Override
    protected void addToStart( List<?> statuses )
    {

    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {

    }
}
