package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.api.services.plus.model.Activity;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusMediaView extends FrameLayout
{
    private ImageView imgvAttachment;
    private ImageView imgvPlay;

    public GPlusMediaView( Context context )
    {
        this( context, null, 0 );
    }

    public GPlusMediaView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public GPlusMediaView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.gplus_media_view, this );
        imgvAttachment = ( ImageView ) findViewById( R.id.imgv_attachment );
        imgvPlay = ( ImageView ) findViewById( R.id.imgv_play );
    }

    public void updateMedia( Activity activity )
    {
        Picasso.with( HashtaggerApp.app )
                .load( activity.getObject().getAttachments().get( 0 ).getImage().getUrl() )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvAttachment );
        if ( "video".equals( activity.getObject().getAttachments().get( 0 ).getObjectType() ) )
        {
            imgvPlay.setVisibility( VISIBLE );
        }
        else
        {
            imgvPlay.setVisibility( GONE );
        }
    }
}
