package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterReplyDialog;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterRetweetDialog;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 6/17/14.
 * <p/>
 * Performs Twitter actions like reply, retweet and favorite
 * This class merely satisfies the pre-conditions for actions
 * like checking if network is available, whether tweet has
 * already been retweeted, showing dialog to enter reply etc.
 * The actual actions is performed by the TwitterAction class.
 */
public class TwitterActionsPerformer
{
    // Interface to get notified when event is done
    // Classes don't actually need to implement this
    // interface since the event notification happens via
    // Otto. This is here just for the sake of contract.
    public static interface OnTwitterActionDoneListener
    {
        @Subscribe
        public void onReplyDone( TwitterReplyDoneEvent event );

        @Subscribe
        public void onRetweetDone( TwitterRetweetDoneEvent event );

        @Subscribe
        public void onFavoriteDone( TwitterFavoriteDoneEvent event );
    }

    // Needed to show dialogs
    private FragmentManager mFragmentManager;

    public TwitterActionsPerformer( FragmentManager manager )
    {
        this.mFragmentManager = manager;
    }

    public void doReply( Status status )
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( HashtaggerApp.app );
            return;
        }
        TwitterReplyDialog dialog = TwitterReplyDialog.newInstance( status );
        dialog.show( mFragmentManager, TwitterReplyDialog.TAG );
    }

    public void doRetweet( Status status )
    {
        if ( status.isRetweeted() )
        {
            Toast.makeText( HashtaggerApp.app, "You have already retweeted this", Toast.LENGTH_SHORT ).show();
            return;
        }
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( HashtaggerApp.app );
            return;
        }
        TwitterRetweetDialog dialog = TwitterRetweetDialog.newInstance( status );
        dialog.show( mFragmentManager, TwitterRetweetDialog.TAG );
    }

    public void doFavorite( Status status )
    {
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( HashtaggerApp.app );
            return;
        }
        new TwitterAction().executeFavoriteAction( status );
    }
}