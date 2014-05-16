package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.CenterContentButton;

import java.util.List;

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
    }

    @Override
    protected void updateButtons( Object result )
    {
        this.activity = ( Activity ) result;
        ccbPlusOne.setOnClickListener( this );
        ccbComment.setOnClickListener( this );
        ccbShare.setOnClickListener( this );
        ccbViewDetails.setOnClickListener( this );
        ccbPlusOne.setText( String.valueOf( activity.getObject().getPlusoners().getTotalItems() ) );
        if ( activity.getObject().getReplies().getTotalItems() != 0 )
        {
            ccbComment.setText( String.valueOf( activity.getObject().getReplies().getTotalItems() ) );
        }
        if ( activity.getObject().getResharers().getTotalItems() != 0 )
        {
            ccbShare.setText( String.valueOf( activity.getObject().getResharers().getTotalItems() ) );
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
            doViewDetails();
        }
        else
        {
            doOpenInBrowser();
        }
    }

    private void doOpenInBrowser()
    {
        Intent i = new Intent( Intent.ACTION_VIEW );
        i.setData( Uri.parse( activity.getObject().getUrl() ) );
        getContext().startActivity( i );
    }

    private void doViewDetails()
    {
        Intent i = new Intent( getContext(), GPlusDetailActivity.class );
        GPlusData.ActivityData.pushActivity( this.activity );
        getContext().startActivity( i );
    }
}
