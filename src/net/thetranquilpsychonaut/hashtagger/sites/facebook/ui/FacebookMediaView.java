package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/17/14.
 */
public class FacebookMediaView extends FrameLayout
{
    private ImageView imgvPicture;
    private ImageView imgvPlay;

    public FacebookMediaView( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookMediaView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookMediaView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_media_view, this );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        imgvPlay = ( ImageView ) findViewById( R.id.imgv_play );
    }

    public void updateMedia( Post post )
    {
        Picasso.with( HashtaggerApp.app )
                .load( post.getPicture().toString() )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvPicture );
        if ( "video".equals( post.getType() ) )
        {
            imgvPlay.setVisibility( VISIBLE );
        }
        else
        {
            imgvPlay.setVisibility( GONE );
        }
    }
}
