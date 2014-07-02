package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.PageDescriptor;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.events.SearchHashtagEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.components.FacebookSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.PersistentData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/10/14.
 */
public class FacebookFragment extends SitesFragment
{
    public static final SimplePageDescriptor DESCRIPTOR = new SimplePageDescriptor( HashtaggerApp.FACEBOOK, HashtaggerApp.FACEBOOK );

    @Override
    protected int getFlatIconResId()
    {
        return R.drawable.facebook_icon_flat;
    }

    @Override
    protected int getFlatIcon170ResId()
    {
        return R.drawable.facebook_icon_flat_170;
    }

    @Override
    protected boolean isUserLoggedIn()
    {
        return AccountPrefs.areFacebookDetailsPresent();
    }

    @Override
    protected void removeUserDetails()
    {
        AccountPrefs.removeFacebookDetails();
    }

    @Override
    protected String getUserName()
    {
        return AccountPrefs.getFacebookUserName();
    }

    @Override
    public PageDescriptor getPageDescriptor()
    {
        return DESCRIPTOR;
    }

    @Override
    protected int getLoginButtonBackgroundId()
    {
        return R.drawable.selector_facebook;
    }

    @Override
    protected void saveData()
    {
        PersistentData.FacebookFragmentData.posts = ( List<Post> ) results;
        PersistentData.FacebookFragmentData.postTypes = resultTypes;
    }

    @Override
    protected String getLoginButtonText()
    {
        return getResources().getString( R.string.str_facebook_login_button_text );
    }

    @Override
    protected String getSiteName()
    {
        return HashtaggerApp.FACEBOOK;
    }

    @Override
    protected SitesSearchHandler initSitesSearchHandler()
    {
        return new FacebookSearchHandler( this );
    }

    @Override
    protected SitesListAdapter initSitesListAdapter()
    {
        return new FacebookListAdapter( getActivity(), 0, results, resultTypes );
    }

    @Override
    protected List<?> initResultsList()
    {
        return null == PersistentData.FacebookFragmentData.posts ?
                new ArrayList<Post>() :
                PersistentData.FacebookFragmentData.posts;
    }

    @Override
    protected List<Integer> initResultTypesList()
    {
        return null == PersistentData.FacebookFragmentData.postTypes ?
                new ArrayList<Integer>() :
                PersistentData.FacebookFragmentData.postTypes;
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
        return HashtaggerApp.FACEBOOK_VALUE;
    }

    @Override
    protected Class<?> getLoginActivityClassName()
    {
        return FacebookLoginActivity.class;
    }

    @Override
    protected void updateResultsAndTypes( int searchType, List<?> searchResults )
    {
        List<Post> newResults = ( List<Post> ) searchResults;
        List<Integer> newResultTypes = new ArrayList<Integer>( newResults.size() );
        for ( Post post : newResults )
        {
            newResultTypes.add( FacebookListAdapter.getPostType( post ) );
        }
        if ( searchType == SearchType.NEWER || searchType == SearchType.TIMED )
        {
            ( ( List<Post> ) results ).addAll( 0, newResults );
            resultTypes.addAll( 0, newResultTypes );
        }
        else
        {
            ( ( List<Post> ) results ).addAll( newResults );
            resultTypes.addAll( newResultTypes );
        }
    }

    @Subscribe
    public void searchHashtag( SearchHashtagEvent event )
    {
        super.searchHashtag( event );
    }
}
