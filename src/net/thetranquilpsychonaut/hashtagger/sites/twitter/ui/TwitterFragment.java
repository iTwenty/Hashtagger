package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.net.Uri;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.SimplePageDescriptor;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.events.SearchHashtagEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterActionClickedEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterAction;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFragmentData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterFragment extends SitesFragment
{
    public static SimplePageDescriptor descriptor = new SimplePageDescriptor( HashtaggerApp.TWITTER, HashtaggerApp.TWITTER );

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
                doReply( status );
                break;
            case TwitterActionClickedEvent.ACTION_RETWEET:
                doRetweet( status );
                break;
            case TwitterActionClickedEvent.ACTION_FAVORITE:
                doFavorite( status );
                break;
            default:
                break;
        }
    }

    private void doReply( Status status )
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( getActivity() );
            return;
        }
        TwitterReplyDialog dialog = TwitterReplyDialog.newInstance( status.getUser().getScreenName(), status.getIdStr() );
        dialog.show( getChildFragmentManager(), TwitterReplyDialog.TAG );
    }

    private void doRetweet( Status status )
    {
        if ( status.isRetweeted() )
        {
            Toast.makeText( getActivity(), "You have already retweeted this", Toast.LENGTH_SHORT ).show();
            return;
        }
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( getActivity() );
            return;
        }
        TwitterRetweetDialog dialog = TwitterRetweetDialog.newInstance( status.getIdStr(), results.indexOf( status ) );
        dialog.show( getChildFragmentManager(), TwitterRetweetDialog.TAG );
    }

    private void doFavorite( Status status )
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( getActivity() );
            return;
        }
        new TwitterAction().executeFavoriteAction( status.getIdStr(), status.isFavorited(), results.indexOf( status ) );
    }

    @Subscribe
    public void onRetweetDone( TwitterRetweetEvent event )
    {
        if ( event.getSuccess() )
        {
            ( ( List<Status> ) results ).get( event.getPosition() ).setRetweeted( true );
            sitesListAdapter.notifyDataSetChanged();
            Toast.makeText( getActivity(), "Retweeted like a champ!", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( getActivity(), "Failed to retweet", Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void onFavoriteDone( TwitterFavoriteEvent event )
    {
        if ( event.getSuccess() )
        {
            ( ( List<Status> ) results ).get( event.getPosition() ).setFavorited( !event.wasFavorited() );
            sitesListAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText( getActivity(), "Failed to favorite", Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void onReplyDone( TwitterReplyEvent event )
    {
        if ( event.getSuccess() )
        {
            Toast.makeText( getActivity(), "Replied like a champ!", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( getActivity(), "Failed to reply", Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void searchHashtag( SearchHashtagEvent event )
    {
        super.searchHashtag( event );
    }
}
