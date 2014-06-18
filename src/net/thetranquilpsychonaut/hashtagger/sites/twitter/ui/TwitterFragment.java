package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.net.Uri;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.PageDescriptor;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.events.SearchHashtagEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterActionClickedEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterActionsPerformer;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragmentData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment implements TwitterActionsPerformer.OnTwitterActionDoneListener
{
    public static final SimplePageDescriptor DESCRIPTOR = new SimplePageDescriptor( HashtaggerApp.TWITTER, HashtaggerApp.TWITTER );
    private TwitterActionsPerformer twitterActionsPerformer;

    @Override
    public void onStart()
    {
        super.onStart();
        twitterActionsPerformer = new TwitterActionsPerformer( getChildFragmentManager() );
    }

    @Override
    public void onStop()
    {
        super.onStop();
        twitterActionsPerformer = null;
    }

    @Override
    protected int getLogoResId()
    {
        return R.drawable.twitter_icon_flat;
    }

    @Override
    protected int getPlainLogoResId()
    {
        return R.drawable.twitter_icon_plain;
    }

    @Override
    protected boolean isUserLoggedIn()
    {
        return AccountPrefs.areTwitterDetailsPresent();
    }

    @Override
    protected void removeUserDetails()
    {
        AccountPrefs.removeTwitterDetails();
    }

    @Override
    protected String getUserName()
    {
        return AccountPrefs.getTwitterUserName();
    }

    @Override
    public PageDescriptor getPageDescriptor()
    {
        return DESCRIPTOR;
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
    protected String getSiteName()
    {
        return HashtaggerApp.TWITTER;
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
    protected void updateResultsAndTypes( int searchType, List<?> searchResults )
    {
        List<Status> newResults = ( List<Status> ) searchResults;
        List<Integer> newResultTypes = new ArrayList<Integer>( newResults.size() );
        for ( Status status : newResults )
        {
            newResultTypes.add( TwitterListAdapter.getStatusType( status ) );
        }
        if ( searchType == SearchType.NEWER || searchType == SearchType.TIMED )
        {
            ( ( List<Status> ) results ).addAll( 0, newResults );
            resultTypes.addAll( 0, newResultTypes );
        }
        else
        {
            ( ( List<Status> ) results ).addAll( newResults );
            resultTypes.addAll( newResultTypes );
        }
    }

    @Override
    protected String getResultText( Object result )
    {
        Status status = ( Status ) result;
        return status.isRetweeted() ? status.getRetweetedStatus().getText() : status.getText();
    }

    @Override
    protected Uri getResultUrl( Object result )
    {
        return UrlModifier.getTwitterStatusUrl( ( Status ) result );
    }

    @Subscribe
    public void onTwitterActionClicked( TwitterActionClickedEvent event )
    {
        Status status = event.getStatus();
        switch ( event.getActionType() )
        {
            case TwitterActionClickedEvent.ACTION_REPLY:
                twitterActionsPerformer.doReply( status );
                break;
            case TwitterActionClickedEvent.ACTION_RETWEET:
                twitterActionsPerformer.doRetweet( status );
                break;
            case TwitterActionClickedEvent.ACTION_FAVORITE:
                twitterActionsPerformer.doFavorite( status );
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onRetweetDone( TwitterRetweetDoneEvent event )
    {
        if ( event.getSuccess() )
        {
            sitesListAdapter.notifyDataSetChanged();
            Toast.makeText( getActivity(), getResources().getString( R.string.str_retweet_success ), Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_retweet_failure ), Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void onFavoriteDone( TwitterFavoriteDoneEvent event )
    {
        if ( event.getSuccess() )
        {
            sitesListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_favorite_failure ), Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void onReplyDone( TwitterReplyDoneEvent event )
    {
        if ( event.getSuccess() )
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_reply_success ), Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( getActivity(), getResources().getString( R.string.str_reply_failure ), Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void searchHashtag( SearchHashtagEvent event )
    {
        super.searchHashtag( event );
    }
}
