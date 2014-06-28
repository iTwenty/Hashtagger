package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.GPlusActionClickedEvent;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.widgets.CenterContentButton;

/**
 * Created by itwenty on 5/7/14.
 */
public class GPlusButtons extends SitesButtons implements View.OnClickListener
{
    private CenterContentButton ccbPlusOne;
    private CenterContentButton ccbComment;
    private CenterContentButton ccbShare;
    private CenterContentButton ccbViewDetails;
    private Activity            activity;

    public GPlusButtons( Context context )
    {
        this( context, null, 0 );
    }

    public GPlusButtons( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public GPlusButtons( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.gplus_buttons, this );
        ccbPlusOne = ( CenterContentButton ) findViewById( R.id.ccb_plusone );
        ccbComment = ( CenterContentButton ) findViewById( R.id.ccb_comment );
        ccbShare = ( CenterContentButton ) findViewById( R.id.ccb_share );
        ccbViewDetails = ( CenterContentButton ) findViewById( R.id.ccb_view_details );
        setCenterDrawable( ccbPlusOne, mShowSmallButtons ? R.drawable.plus_small : R.drawable.plus );
        setCenterDrawable( ccbComment, mShowSmallButtons ? R.drawable.comment_small : R.drawable.comment );
        setCenterDrawable( ccbShare, mShowSmallButtons ? R.drawable.share_small : R.drawable.share );
        setCenterDrawable( ccbViewDetails, mShowSmallButtons ? R.drawable.view_details_small : R.drawable.view_details );
        ccbViewDetails.setVisibility( mShowViewDetailsButton ? VISIBLE : GONE );
    }

    @Override
    protected void updateButtons( Object result )
    {
        this.activity = ( Activity ) result;
        ccbPlusOne.setOnClickListener( this );
        ccbComment.setOnClickListener( this );
        ccbShare.setOnClickListener( this );
        ccbViewDetails.setOnClickListener( this );
        if ( activity.getObject().getPlusoners().getTotalItems() != 0 )
        {
            ccbPlusOne.setText( String.valueOf( activity.getObject().getPlusoners().getTotalItems() ) );
        }
        else
        {
            ccbPlusOne.setText( "" );
        }
        if ( activity.getObject().getReplies().getTotalItems() != 0 )
        {
            ccbComment.setText( String.valueOf( activity.getObject().getReplies().getTotalItems() ) );
        }
        else
        {
            ccbComment.setText( "" );
        }
        if ( activity.getObject().getResharers().getTotalItems() != 0 )
        {
            ccbShare.setText( String.valueOf( activity.getObject().getResharers().getTotalItems() ) );
        }
        else
        {
            ccbShare.setText( "" );
        }
    }

    @Override
    protected void clearButtons()
    {
        this.activity = null;
        ccbPlusOne.setOnClickListener( null );
        ccbComment.setOnClickListener( null );
        ccbShare.setOnClickListener( null );
        ccbViewDetails.setOnClickListener( null );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( ccbViewDetails ) )
        {
            GPlusDetailActivity.createAndStartActivity( activity, getContext() );
        }
        else if ( v.equals( ccbPlusOne ) )
        {
            // Subscriber : GPlusFragment : onGPlusActionClicked()
            HashtaggerApp.bus.post( new GPlusActionClickedEvent( activity, GPlusActionClickedEvent.ACTION_PLUS_ONE ) );
        }
        else if ( v.equals( ccbComment ) )
        {
            // Subscriber : GPlusFragment : onGPlusActionClicked()
            HashtaggerApp.bus.post( new GPlusActionClickedEvent( activity, GPlusActionClickedEvent.ACTION_REPLY ) );
        }
        else if ( v.equals( ccbShare ) )
        {
            // Subscriber : GPlusFragment : onGPlusActionClicked()
            HashtaggerApp.bus.post( new GPlusActionClickedEvent( activity, GPlusActionClickedEvent.ACTION_RESHARE ) );
        }
    }
}
