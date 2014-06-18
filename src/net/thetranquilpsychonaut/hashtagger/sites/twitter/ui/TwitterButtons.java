package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.TwitterActionClickedEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.CenterContentButton;


/**
 * Created by itwenty on 4/16/14.
 */
public class TwitterButtons extends SitesButtons implements View.OnClickListener
{
    public static int pos = -1;
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
            ccbRetweet.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.retweet_on_small ), null, null, null );
        }
        else
        {
            ccbRetweet.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.retweet_small ), null, null, null );
        }
    }


    private void setFavorited( boolean isFavorited )
    {
        if ( isFavorited )
        {
            ccbFavorite.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.favorite_on_small ), null, null, null );
        }
        else
        {
            ccbFavorite.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.favorite_small ), null, null, null );
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
            // Subscriber : TwitterFragment : onTwitterActionClicked()
            HashtaggerApp.bus.post( new TwitterActionClickedEvent( status, TwitterActionClickedEvent.ACTION_REPLY ) );
        }
        if ( v.equals( ccbRetweet ) )
        {
            // Subscriber : TwitterFragment : onTwitterActionClicked()
            HashtaggerApp.bus.post( new TwitterActionClickedEvent( status, TwitterActionClickedEvent.ACTION_RETWEET ) );
        }
        if ( v.equals( ccbFavorite ) )
        {
            // Subscriber : TwitterFragment : onTwitterActionClicked()
            HashtaggerApp.bus.post( new TwitterActionClickedEvent( status, TwitterActionClickedEvent.ACTION_FAVORITE ) );
        }
        if ( v.equals( ccbViewDetails ) )
        {
            doViewDetails();
        }
    }

    private void doViewDetails()
    {
        TwitterDetailActivity.createAndStartActivity( status, getContext() );
    }

    @Override
    public void doOpenInBrowser()
    {
        Intent i = new Intent( Intent.ACTION_VIEW );
        i.setData( UrlModifier.getTwitterStatusUrl( status ) );
        getContext().startActivity( i );
    }
}
