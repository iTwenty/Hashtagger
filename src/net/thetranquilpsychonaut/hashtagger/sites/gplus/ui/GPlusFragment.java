package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.net.Uri;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusFragment extends SitesFragment
{
    public static SimplePageDescriptor descriptor = new SimplePageDescriptor( HashtaggerApp.GPLUS, HashtaggerApp.GPLUS );

    @Override
    protected SitesUserHandler initSitesUserHandler()
    {
        return new GPlusUserHandler( this );
    }

    @Override
    protected SitesSearchHandler initSitesSearchHandler()
    {
        return new GPlusSearchHandler( this );
    }

    @Override
    protected SitesListAdapter initSitesListAdapter()
    {
        return new GPlusListAdapter( getActivity(), 0, results, resultTypes );

    }

    @Override
    protected List<?> initResultsList()
    {
        return new ArrayList<Activity>();
    }

    @Override
    protected int getLogo()
    {
        return R.drawable.gplus_logo;
    }

    @Override
    protected int getLoginButtonBackgroundId()
    {
        return R.drawable.selector_gplus_icon_background;
    }

    @Override
    protected String getLoginButtonText()
    {
        return getResources().getString( R.string.str_gplus_login_button_text );
    }

    @Override
    protected int getLoggedInToastTextId()
    {
        return R.string.str_toast_gplus_logged_in_as;
    }

    @Override
    protected int getLoginFailureToastTextId()
    {
        return R.string.str_toast_gplus_login_failed;
    }

    @Override
    protected int getLoginRequestCode()
    {
        return HashtaggerApp.GPLUS_VALUE;
    }

    @Override
    protected Class<?> getLoginActivityClassName()
    {
        return GPlusLoginActivity.class;
    }

    @Override
    protected void addToEnd( List<?> searchResults )
    {
        ( ( List<Activity> ) results ).addAll( ( List<Activity> ) searchResults );
    }

    @Override
    protected void addToStart( List<?> searchResults )
    {
        ( ( List<Activity> ) results ).addAll( 0, ( List<Activity> ) searchResults );
    }

    @Override
    protected Uri getResultUrl( Object result )
    {
        return Helper.getGPlusActivityUrl( ( Activity ) result );
    }
}
