package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
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
    private CenterContentButton ccbShare;
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
        ccbShare = ( CenterContentButton ) findViewById( R.id.ccb_share );
        ccbViewDetails = ( CenterContentButton ) findViewById( R.id.ccb_view_details );
    }

    @Override
    protected void updateButtons( Object result )
    {
        this.post = ( Post ) result;
        ccbLike.setOnClickListener( this );
        ccbComment.setOnClickListener( this );
        ccbShare.setOnClickListener( this );
        ccbViewDetails.setOnClickListener( this );
        ccbLike.setText( post.getLikes() != null && !Helper.isNullOrEmpty( post.getLikes().getData() ) ? String.valueOf( post.getLikes().getData().size() ) : "" );
        ccbComment.setText( post.getComments() != null && !Helper.isNullOrEmpty( post.getComments().getData() ) ? String.valueOf( post.getComments().getData().size() ) : "" );
        ccbShare.setText( post.getShares() != null && post.getShares().getCount() != 0 ? String.valueOf( post.getShares().getCount() ) : "" );
    }

    @Override
    protected void clearButtons()
    {
        ccbLike.setOnClickListener( null );
        ccbComment.setOnClickListener( null );
        ccbShare.setOnClickListener( null );
        ccbViewDetails.setOnClickListener( null );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( ccbLike ) )
        {
            doLike();
        }
        else if ( v.equals( ccbComment ) )
        {
            doComment();
        }
        else if ( v.equals( ccbShare ) )
        {
            doShare();
        }
        else if ( v.equals( ccbViewDetails ) )
        {
            doViewDetails();
        }
    }

    private void doViewDetails()
    {
        Intent i = new Intent( getContext(), FacebookDetailActivity.class );
        i.putExtra( FacebookDetailActivity.POST_KEY, post );
        getContext().startActivity( i );
    }

    private void doShare()
    {

        Toast.makeText( getContext(), "Sorry, Facebook shares are not implemented yet", Toast.LENGTH_SHORT ).show();
        //doOpenInBrowser();
    }

    private void doComment()
    {
        Toast.makeText( getContext(), "Sorry, Facebook comments are not implemented yet", Toast.LENGTH_SHORT ).show();
        //doOpenInBrowser();
    }

    private void doLike()
    {
        Toast.makeText( getContext(), "Sorry, Facebook likes are not implemented yet", Toast.LENGTH_SHORT ).show();
        //doOpenInBrowser();
    }

    @Override
    public void doOpenInBrowser()
    {
        Intent i = new Intent( Intent.ACTION_VIEW );
        i.setData( Helper.getFacebookPostUrl( post ) );
        getContext().startActivity( i );
    }
}
