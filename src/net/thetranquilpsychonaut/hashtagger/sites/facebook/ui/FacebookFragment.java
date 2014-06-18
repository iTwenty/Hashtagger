package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.net.Uri;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.PageDescriptor;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.events.FacebookActionClickedEvent;
import net.thetranquilpsychonaut.hashtagger.events.SearchHashtagEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.components.FacebookSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragmentData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/10/14.
 */
public class FacebookFragment extends SitesFragment
{
    public static final SimplePageDescriptor DESCRIPTOR = new SimplePageDescriptor( HashtaggerApp.FACEBOOK, HashtaggerApp.FACEBOOK );

    @Override
    protected int getLogoResId()
    {
        return R.drawable.facebook_icon_flat;
    }

    @Override
    protected int getPlainLogoResId()
    {
        return R.drawable.facebook_icon_plain;
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
        return R.drawable.selector_facebook_icon_background;
    }

    @Override
    protected void saveData()
    {
        SitesFragmentData.Facebook.posts = ( List<Post> ) results;
        SitesFragmentData.Facebook.postTypes = resultTypes;
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
        return null == SitesFragmentData.Facebook.posts ? new ArrayList<Post>() : SitesFragmentData.Facebook.posts;
    }

    @Override
    protected List<Integer> initResultTypesList()
    {
        return null == SitesFragmentData.Facebook.postTypes ? new ArrayList<Integer>() : SitesFragmentData.Facebook.postTypes;
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

    @Override
    protected String getResultText( Object result )
    {
        return ( ( Post ) result ).getMessage();
    }

    @Override
    protected Uri getResultUrl( Object result )
    {
        return UrlModifier.getFacebookPostUrl( ( Post ) result );
    }

    @Subscribe
    public void searchHashtag( SearchHashtagEvent event )
    {
        super.searchHashtag( event );
    }

    @Subscribe
    public void onFacebookActionClicked( FacebookActionClickedEvent event )
    {
        FacebookActionsFragment fragment = FacebookActionsFragment.newInstance( event.getPost(), event.getActionType() );
        fragment.show( getChildFragmentManager(), FacebookActionsFragment.TAG );
    }
}
