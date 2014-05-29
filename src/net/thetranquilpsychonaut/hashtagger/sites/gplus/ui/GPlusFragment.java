package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.net.Uri;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusUserHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragmentData;
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
        return null == SitesFragmentData.GPlus.activities ? new ArrayList<Activity>() : SitesFragmentData.GPlus.activities;
    }

    @Override
    protected List<Integer> initResultTypesList()
    {
        return null == SitesFragmentData.GPlus.activityTypes ? new ArrayList<Integer>() : SitesFragmentData.GPlus.activityTypes;
    }

    @Override
    protected int getSketchLogoResId()
    {
        return R.drawable.gplus_sketch;
    }

    @Override
    protected int getLogoResId()
    {
        return R.drawable.gplus_icon_flat;
    }

    @Override
    protected int getLoginButtonBackgroundId()
    {
        return R.drawable.selector_gplus_icon_background;
    }

    @Override
    protected void saveData()
    {
        SitesFragmentData.GPlus.activities = ( List<Activity> ) results;
        SitesFragmentData.GPlus.activityTypes = resultTypes;
    }

    @Override
    protected String getLoginButtonText()
    {
        return getResources().getString( R.string.str_gplus_login_button_text );
    }

    @Override
    protected String getSiteName()
    {
        return HashtaggerApp.GPLUS;
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
    protected void updateResultsAndTypes( SearchType searchType, List<?> searchResults )
    {
        List<Activity> newResults = ( List<Activity> ) searchResults;
        List<Integer> newResultTypes = new ArrayList<Integer>( newResults.size() );
        for ( Activity activity : newResults )
        {
            newResultTypes.add( GPlusListAdapter.getActivityType( activity ) );
        }
        if ( searchType == SearchType.NEWER || searchType == SearchType.TIMED )
        {
            ( ( List<Activity> ) results ).addAll( 0, newResults );
            resultTypes.addAll( 0, newResultTypes );
        }
        else
        {
            ( ( List<Activity> ) results ).addAll( newResults );
            resultTypes.addAll( newResultTypes );
        }
    }


    @Override
    protected String getResultText( Object result )
    {
        return ( ( Activity ) result ).getObject().getOriginalContent();
    }

    @Override
    protected Uri getResultUrl( Object result )
    {
        return Helper.getGPlusActivityUrl( ( Activity ) result );
    }
}
