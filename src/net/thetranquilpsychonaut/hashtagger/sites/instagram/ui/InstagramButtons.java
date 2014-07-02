package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.CenterContentButton;

/**
 * Created by itwenty on 6/25/14.
 */
public class InstagramButtons extends SitesButtons implements View.OnClickListener
{
    private CenterContentButton ccbLike;
    private CenterContentButton ccbComment;
    private CenterContentButton ccbViewDetails;
    private Media               media;

    public InstagramButtons( Context context )
    {
        this( context, null, 0 );
    }

    public InstagramButtons( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public InstagramButtons( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.instagram_buttons, this );
        ccbLike = ( CenterContentButton ) findViewById( R.id.ccb_like );
        ccbComment = ( CenterContentButton ) findViewById( R.id.ccb_comment );
        ccbViewDetails = ( CenterContentButton ) findViewById( R.id.ccb_view_details );
        setCenterDrawable( ccbLike, mShowSmallButtons ? R.drawable.instagram_like_small : R.drawable.instagram_like );
        setCenterDrawable( ccbComment, mShowSmallButtons ? R.drawable.comment_small : R.drawable.comment );
        setCenterDrawable( ccbViewDetails, mShowSmallButtons ? R.drawable.view_details_small : R.drawable.view_details );
        ccbViewDetails.setVisibility( mShowViewDetailsButton ? VISIBLE : GONE );
    }

    @Override
    protected void updateButtons( Object result )
    {
        this.media = ( Media ) result;
        ccbLike.setOnClickListener( this );
        ccbComment.setOnClickListener( this );
        ccbViewDetails.setOnClickListener( this );
        if ( media.getLikes() != null && media.getLikes().getCount() != 0 )
        {
            ccbLike.setText( Integer.toString( media.getLikes().getCount() ) );
        }
        else
        {
            ccbLike.setText( "" );
        }
        if ( media.getComments() != null && media.getComments().getCount() != 0 )
        {
            ccbComment.setText( Integer.toString( media.getComments().getCount() ) );
        }
        else
        {
            ccbComment.setText( "" );
        }
        setLiked( media.isUserHasLiked() );
    }

    private void setLiked( boolean userHasLiked )
    {
        if ( userHasLiked )
        {
            setCenterDrawable( ccbLike, mShowSmallButtons ? R.drawable.instagram_like_on_small : R.drawable.instagram_like_on );
        }
        else
        {
            setCenterDrawable( ccbLike, mShowSmallButtons ? R.drawable.instagram_like_small : R.drawable.instagram_like );
        }
    }

    @Override
    protected void clearButtons()
    {
        this.media = null;
        ccbLike.setOnClickListener( null );
        ccbComment.setOnClickListener( null );
        ccbViewDetails.setOnClickListener( null );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( ccbLike ) )
        {
            if ( !HashtaggerApp.isNetworkConnected() )
            {
                Helper.showNoNetworkToast( getContext() );
                return;
            }
            InstagramActions.executeLikeAction( media );
        }
        else if ( v.equals( ccbComment ) )
        {

        }
        else if ( v.equals( ccbViewDetails ) )
        {
            InstagramDetailActivity.createAndStartActivity( media, getContext() );
        }
    }
}
