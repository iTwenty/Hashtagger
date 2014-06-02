package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.ArrayList;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterPhotoRow extends TwitterListRow implements View.OnClickListener
{
    private ImageView imgvMediaThumb;

    protected TwitterPhotoRow( Context context )
    {
        super( context );
    }

    protected TwitterPhotoRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected TwitterPhotoRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.twitter_photo_row, this );
        imgvMediaThumb = ( ImageView ) findViewById( R.id.imgv_thumbnail );
        imgvMediaThumb.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected TwitterHeader initTwitterHeader()
    {
        return ( TwitterHeader ) findViewById( R.id.twitter_header );
    }

    @Override
    protected TextView initStatusText()
    {
        return ( TextView ) findViewById( R.id.tv_status_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.twitter_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        Picasso.with( getContext() )
                .load( status.getMediaEntities()[0].getMediaURL() + ":thumb" )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvMediaThumb );
    }

    @Override
    public void onClick( View v )
    {
        ArrayList<String> imageUrls = new ArrayList<String>( 1 );
        imageUrls.add( Helper.getTwitterLargePhotoUrl( status.getMediaEntities()[0].getMediaURL() ) );
        ViewAlbumActivity.createAndStartActivity( getContext(), imageUrls, 0 );
    }
}
