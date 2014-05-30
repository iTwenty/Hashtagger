package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;

/**
 * Created by itwenty on 5/31/14.
 */
public class GPlusPhotoRow extends GPlusListRow implements View.OnClickListener
{
    private ImageView imgvThumbnail;

    protected GPlusPhotoRow( Context context )
    {
        super( context );
    }

    protected GPlusPhotoRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected GPlusPhotoRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.gplus_photo_row, this );
        imgvThumbnail = ( ImageView ) findViewById( R.id.imgv_thumbnail );
        imgvThumbnail.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected GPlusHeader initGPlusHeader()
    {
        return ( GPlusHeader ) findViewById( R.id.gplus_header );
    }

    @Override
    protected TextView initActivityText()
    {
        return ( TextView ) findViewById( R.id.tv_activity_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.gplus_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        Picasso.with( getContext() )
                .load( activity.getObject().getAttachments().get( 0 ).getImage().getUrl() )
                .error( R.drawable.drawable_image_loading )
                .into( imgvThumbnail );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imgvThumbnail ) )
        {
            Intent i = new Intent( getContext(), ViewImageActivity.class );
            String imageUrl;
            if ( null != activity.getObject().getAttachments().get( 0 ).getFullImage() )
            {
                imageUrl = activity.getObject().getAttachments().get( 0 ).getFullImage().getUrl();
            }
            else
            {
                imageUrl = activity.getObject().getAttachments().get( 0 ).getImage().getUrl();
            }
            i.putExtra( ViewImageActivity.IMAGE_URL_KEY, imageUrl );
            getContext().startActivity( i );
        }
    }
}
