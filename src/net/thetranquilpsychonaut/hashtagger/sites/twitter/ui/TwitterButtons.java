package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterActions;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.CenterContentButton;


/**
 * Created by itwenty on 4/16/14.
 */
public class TwitterButtons extends SitesButtons implements View.OnClickListener
{
    private CenterContentButton ccbReply;
    private CenterContentButton ccbRetweet;
    private CenterContentButton ccbFavorite;
    private CenterContentButton ccbViewDetails;
    private Status              status;

    public TwitterButtons( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterButtons( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterButtons( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.twitter_buttons, this );
        ccbReply = ( CenterContentButton ) findViewById( R.id.ccb_reply );
        ccbRetweet = ( CenterContentButton ) findViewById( R.id.ccb_retweet );
        ccbFavorite = ( CenterContentButton ) findViewById( R.id.ccb_favorite );
        ccbViewDetails = ( CenterContentButton ) findViewById( R.id.ccb_view_details );
        setCenterDrawable( ccbReply, mShowSmallButtons ? R.drawable.reply_small : R.drawable.reply );
        setCenterDrawable( ccbRetweet, mShowSmallButtons ? R.drawable.retweet_small : R.drawable.retweet );
        setCenterDrawable( ccbFavorite, mShowSmallButtons ? R.drawable.favorite_small : R.drawable.favorite );
        setCenterDrawable( ccbViewDetails, mShowSmallButtons ? R.drawable.view_details_small : R.drawable.view_details );
        ccbViewDetails.setVisibility( mShowViewDetailsButton ? VISIBLE : GONE );
    }

    @Override
    protected void updateButtons( Object result )
    {
        this.status = ( Status ) result;
        ccbReply.setOnClickListener( this );
        ccbRetweet.setOnClickListener( this );
        ccbFavorite.setOnClickListener( this );
        ccbViewDetails.setOnClickListener( this );
        ccbRetweet.setText( status.getRetweetCount() != 0 ? String.valueOf( status.getRetweetCount() ) : "" );
        ccbFavorite.setText( status.getFavoriteCount() != 0 ? String.valueOf( status.getFavoriteCount() ) : "" );
        setRetweeted( status.isRetweeted() );
        setFavorited( status.isFavorited() );
    }

    private void setRetweeted( boolean isRetweeted )
    {
        if ( isRetweeted )
        {
            setCenterDrawable( ccbRetweet, mShowSmallButtons ? R.drawable.retweet_on_small : R.drawable.retweet_on );
        }
        else
        {
            setCenterDrawable( ccbRetweet, mShowSmallButtons ? R.drawable.retweet_small : R.drawable.retweet );
        }
    }


    private void setFavorited( boolean isFavorited )
    {
        if ( isFavorited )
        {
            setCenterDrawable( ccbFavorite, mShowSmallButtons ? R.drawable.favorite_on_small : R.drawable.favorite_on );
        }
        else
        {
            setCenterDrawable( ccbFavorite, mShowSmallButtons ? R.drawable.favorite_small : R.drawable.favorite );
        }
    }

    @Override
    protected void clearButtons()
    {
        this.status = null;
        ccbReply.setOnClickListener( null );
        ccbRetweet.setOnClickListener( null );
        ccbFavorite.setOnClickListener( null );
        ccbViewDetails.setOnClickListener( null );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( ccbReply ) )
        {
            if ( !HashtaggerApp.isNetworkConnected() )
            {
                Helper.showNoNetworkToast( getContext() );
                return;
            }
            TwitterReplyDialog dialog = TwitterReplyDialog.newInstance( status );
            dialog.show( ( ( FragmentActivity ) getContext() ).getSupportFragmentManager(), TwitterReplyDialog.TAG );
        }
        if ( v.equals( ccbRetweet ) )
        {
            if ( status.isRetweeted() )
            {
                Toast.makeText( HashtaggerApp.app, "You have already retweeted this", Toast.LENGTH_SHORT ).show();
                return;
            }
            if ( !HashtaggerApp.isNetworkConnected() )
            {
                Helper.showNoNetworkToast( getContext() );
                return;
            }
            TwitterRetweetDialog dialog = TwitterRetweetDialog.newInstance( status );
            dialog.show( ( ( FragmentActivity ) getContext() ).getSupportFragmentManager(), TwitterRetweetDialog.TAG );
        }
        if ( v.equals( ccbFavorite ) )
        {
            if ( !HashtaggerApp.isNetworkConnected() )
            {
                Helper.showNoNetworkToast( getContext() );
                return;
            }
            TwitterActions.executeFavoriteAction( status );
        }
        if ( v.equals( ccbViewDetails ) )
        {
            TwitterDetailActivity.createAndStartActivity( status, getContext() );
        }
    }
}
