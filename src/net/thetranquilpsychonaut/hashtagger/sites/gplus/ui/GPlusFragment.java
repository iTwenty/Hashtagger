package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.PageDescriptor;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.events.GPlusActionClickedEvent;
import net.thetranquilpsychonaut.hashtagger.events.SearchHashtagEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.PersistentData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusFragment extends SitesFragment
{
    public static final SimplePageDescriptor DESCRIPTOR = new SimplePageDescriptor( HashtaggerApp.GPLUS, HashtaggerApp.GPLUS );

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
        return null == PersistentData.GPlusFragmentData.activities ?
                new ArrayList<Activity>() :
                PersistentData.GPlusFragmentData.activities;
    }

    @Override
    protected List<Integer> initResultTypesList()
    {
        return null == PersistentData.GPlusFragmentData.activityTypes ?
                new ArrayList<Integer>() :
                PersistentData.GPlusFragmentData.activityTypes;
    }

    @Override
    protected int getFlatIconResId()
    {
        return R.drawable.gplus_icon_flat;
    }

    @Override
    protected int getFlatIcon170ResId()
    {
        return R.drawable.gplus_icon_flat_170;
    }

    @Override
    protected boolean isUserLoggedIn()
    {
        return AccountPrefs.areGPlusDetailsPresent();
    }

    @Override
    protected void removeUserDetails()
    {
        AccountPrefs.removeGPlusDetails();
    }

    @Override
    protected String getUserName()
    {
        return AccountPrefs.getGPlusUserName();
    }

    @Override
    public PageDescriptor getPageDescriptor()
    {
        return DESCRIPTOR;
    }

    @Override
    protected int getLoginButtonBackgroundId()
    {
        return R.drawable.selector_gplus;
    }

    @Override
    protected void saveData()
    {
        PersistentData.GPlusFragmentData.activities = ( List<Activity> ) results;
        PersistentData.GPlusFragmentData.activityTypes = resultTypes;
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
    protected void updateResultsAndTypes( int searchType, List<?> searchResults )
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

    @Subscribe
    public void searchHashtag( SearchHashtagEvent event )
    {
        super.searchHashtag( event );
    }

    @Subscribe
    public void onGPlusActionClicked( GPlusActionClickedEvent event )
    {
        GPlusActionsFragment fragment = GPlusActionsFragment.newInstance( event.getActivity(), event.getActionType() );
        fragment.show( getChildFragmentManager(), fragment.TAG );
    }
}
