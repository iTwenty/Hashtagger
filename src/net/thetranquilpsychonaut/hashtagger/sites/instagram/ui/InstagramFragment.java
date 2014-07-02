package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.widget.Toast;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.PageDescriptor;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.events.InstagramLikeDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.SearchHashtagEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.components.InstagramSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.PersistentData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramFragment extends SitesFragment
{
    public static final PageDescriptor DESCRIPTOR = new SimplePageDescriptor( HashtaggerApp.INSTAGRAM, HashtaggerApp.INSTAGRAM );

    @Override
    protected int getLoginButtonBackgroundId()
    {
        return R.drawable.selector_instragram;
    }

    @Override
    protected void saveData()
    {
        PersistentData.InstagramFragmentData.medias = ( List<Media> ) results;
        PersistentData.InstagramFragmentData.mediaTypes = resultTypes;
    }

    @Override
    protected String getLoginButtonText()
    {
        return getResources().getString( R.string.str_instagram_login_button_text );
    }

    @Override
    protected String getSiteName()
    {
        return HashtaggerApp.INSTAGRAM;
    }

    @Override
    protected SitesSearchHandler initSitesSearchHandler()
    {
        return new InstagramSearchHandler( this );
    }

    @Override
    protected SitesListAdapter initSitesListAdapter()
    {
        return new InstagramListAdapter( getActivity(), 0, results, resultTypes );
    }

    @Override
    protected List<?> initResultsList()
    {
        return null == PersistentData.InstagramFragmentData.medias ?
                new ArrayList<Media>() :
                PersistentData.InstagramFragmentData.medias;
    }

    @Override
    protected List<Integer> initResultTypesList()
    {
        return null == PersistentData.InstagramFragmentData.mediaTypes ?
                new ArrayList<Integer>() :
                PersistentData.InstagramFragmentData.mediaTypes;
    }

    @Override
    protected int getFlatIconResId()
    {
        return R.drawable.instagram_icon_flat;
    }

    @Override
    protected int getFlatIcon170ResId()
    {
        return R.drawable.instagram_icon_flat_170;
    }

    @Override
    protected boolean isUserLoggedIn()
    {
        return AccountPrefs.areInstagramDetailsPresent();
    }

    @Override
    protected void removeUserDetails()
    {
        AccountPrefs.removeInstagramDetails();
    }

    @Override
    protected String getUserName()
    {
        return AccountPrefs.getInstagramUserName();
    }

    @Override
    public PageDescriptor getPageDescriptor()
    {
        return DESCRIPTOR;
    }

    @Override
    protected int getLoggedInToastTextId()
    {
        return R.string.str_toast_instagram_logged_in_as;
    }

    @Override
    protected int getLoginFailureToastTextId()
    {
        return R.string.str_toast_instagram_login_failed;
    }

    @Override
    protected int getLoginRequestCode()
    {
        return HashtaggerApp.INSTAGRAM_VALUE;
    }

    @Override
    protected Class<?> getLoginActivityClassName()
    {
        return InstagramLoginActivity.class;
    }

    @Override
    protected void updateResultsAndTypes( int searchType, List<?> searchResults )
    {
        List<Media> newResults = ( List<Media> ) searchResults;
        List<Integer> newResultTypes = new ArrayList<Integer>( newResults.size() );
        for ( Media media : newResults )
        {
            newResultTypes.add( InstagramListAdapter.getMediaType( media ) );
        }
        ( ( List<Media> ) results ).addAll( newResults );
        resultTypes.addAll( newResultTypes );
    }

    @Subscribe
    public void searchHashtag( SearchHashtagEvent event )
    {
        super.searchHashtag( event );
    }

    @Subscribe
    public void onInstagramLikeDone( InstagramLikeDoneEvent event )
    {
        if ( event.isSuccess() )
        {
            sitesListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(),
                    event.getMedia().isUserHasLiked() ?
                            getResources().getString( R.string.str_instagram_delete_like_failed ) :
                            getResources().getString( R.string.str_instagram_post_like_failed ),
                    Toast.LENGTH_SHORT )
                    .show();
        }
    }
}
