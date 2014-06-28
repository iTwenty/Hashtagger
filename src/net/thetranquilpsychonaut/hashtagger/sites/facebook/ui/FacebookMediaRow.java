package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;
import net.thetranquilpsychonaut.hashtagger.widgets.VideoThumbnail;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookMediaRow extends FacebookListRow implements View.OnClickListener
{
    private VideoThumbnail imgvThumbnail;

    protected FacebookMediaRow( Context context )
    {
        super( context );
    }

    protected FacebookMediaRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookMediaRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_media_row, this );
        imgvThumbnail = ( VideoThumbnail ) findViewById( R.id.imgv_thumbnail );
        imgvThumbnail.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected LinkifiedTextView initPostText()
    {
        return ( LinkifiedTextView ) findViewById( R.id.tv_post_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.facebook_buttons );
    }

    @Override
    protected SitesHeader initSitesHeader()
    {
        return ( SitesHeader ) findViewById( R.id.facebook_header );
    }

    @Override
    protected ImageView initPopupMenuAnchor()
    {
        return ( ImageView ) findViewById( R.id.popup_menu_anchor );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        Helper.debug( post.getPicture() );
        Picasso.with( getContext() )
                .load( post.getPicture() )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvThumbnail.getVideoThumbnail() );
        imgvThumbnail.showPlayButton( TextUtils.equals( "video", post.getType() ) );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imgvThumbnail ) )
        {
            if ( TextUtils.equals( "photo", post.getType() ) )
            {
                ViewAlbumActivity.createAndStartActivity( getContext(),
                        post.getName(),
                        Helper.createStringArrayList( UrlModifier.getFacebookLargePhotoUrl( post.getPicture().toString() ) ),
                        0 );
            }
            if ( TextUtils.equals( "video", post.getType() ) )
            {
                Intent i = new Intent( Intent.ACTION_VIEW );
                i.setData( Uri.parse( post.getLink() ) );
                getContext().startActivity( i );
            }
        }
    }
}
