package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;

import java.util.ArrayList;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookPhotoRow extends FacebookListRow implements View.OnClickListener
{
    private ImageView imgvThumbnail;

    protected FacebookPhotoRow( Context context )
    {
        super( context );
    }

    protected FacebookPhotoRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookPhotoRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_photo_row, this );
        imgvThumbnail = ( ImageView ) findViewById( R.id.imgv_thumbnail );
        imgvThumbnail.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected FacebookHeader initFacebookHeader()
    {
        return ( FacebookHeader ) findViewById( R.id.facebook_header );
    }

    @Override
    protected TextView initPostText()
    {
        return ( TextView ) findViewById( R.id.tv_post_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.facebook_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        Picasso.with( getContext() )
                .load( post.getPicture().toString() )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvThumbnail );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imgvThumbnail ) )
        {
            ArrayList<String> imageUrl = new ArrayList<String>( 1 );
            imageUrl.add( post.getPicture().toString() );
            ViewAlbumActivity.createAndStartActivity( getContext(), imageUrl, 0 );
        }
    }
}
