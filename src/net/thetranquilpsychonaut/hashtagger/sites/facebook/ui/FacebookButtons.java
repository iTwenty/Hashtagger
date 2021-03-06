package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.CenterContentButton;

/**
 * Created by itwenty on 4/18/14.
 */
public class FacebookButtons extends SitesButtons implements View.OnClickListener
{
    private CenterContentButton ccbLike;
    private CenterContentButton ccbComment;
    private CenterContentButton ccbViewDetails;
    private Post                post;

    public FacebookButtons( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookButtons( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookButtons( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_buttons, this );
        ccbLike = ( CenterContentButton ) findViewById( R.id.ccb_like );
        ccbComment = ( CenterContentButton ) findViewById( R.id.ccb_comment );
        ccbViewDetails = ( CenterContentButton ) findViewById( R.id.ccb_view_details );
        setCenterDrawable( ccbLike, mShowSmallButtons ? R.drawable.facebook_like_small : R.drawable.facebook_like );
        setCenterDrawable( ccbComment, mShowSmallButtons ? R.drawable.comment_small : R.drawable.comment );
        setCenterDrawable( ccbViewDetails, mShowSmallButtons ? R.drawable.view_details_small : R.drawable.view_details );
        ccbViewDetails.setVisibility( mShowViewDetailsButton ? VISIBLE : GONE );
    }

    @Override
    public void updateButtons( Object result )
    {
        this.post = ( Post ) result;
        ccbLike.setOnClickListener( this );
        ccbComment.setOnClickListener( this );
        ccbViewDetails.setOnClickListener( this );
        if ( null != post.getLikes() && !Helper.isNullOrEmpty( post.getLikes().getData() ) )
        {
            ccbLike.setText( String.valueOf( post.getLikes().getData().size() ) );
        }
        else
        {
            ccbLike.setText( "" );
        }
        if ( null != post.getComments() && !Helper.isNullOrEmpty( post.getComments().getData() ) )
        {
            ccbComment.setText( String.valueOf( post.getComments().getData().size() ) );
        }
        else
        {
            ccbComment.setText( "" );
        }
    }

    @Override
    protected void clearButtons()
    {
        ccbLike.setOnClickListener( null );
        ccbComment.setOnClickListener( null );
        ccbViewDetails.setOnClickListener( null );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( ccbLike ) )
        {
            Toast.makeText( getContext(), "Not available yet.", Toast.LENGTH_SHORT ).show();
        }
        else if ( v.equals( ccbComment ) )
        {
            Toast.makeText( getContext(), "Not available yet.", Toast.LENGTH_SHORT ).show();
        }
        else if ( v.equals( ccbViewDetails ) )
        {
            FacebookDetailActivity.createAndStartActivity( post, getContext() );
        }
    }
}
