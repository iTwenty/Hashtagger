package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/18/14.
 */
public class FacebookObjectView extends FrameLayout
{
    private ImageView imgvPicture;
    private ImageView imgvPlayButton;

    public FacebookObjectView( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookObjectView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookObjectView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_object_view, this );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        imgvPlayButton = ( ImageView ) findViewById( R.id.imgv_play_button );
    }

    public void showObjectFromPost( final Post post )
    {
        UrlImageViewHelper.setUrlDrawable(
                imgvPicture,
                post.getPicture().toString().replace( "_s.", "_o." ),
                null,
                HashtaggerApp.CACHE_DURATION_MS,
                new UrlImageViewCallback()
                {
                    @Override
                    public void onLoaded( ImageView imageView, Bitmap bitmap, String s, boolean b )
                    {
                        if ( "video".equals( post.getType() ) )
                        {
                            imgvPlayButton.setVisibility( VISIBLE );
                        }
                        else
                        {
                            imgvPlayButton.setVisibility( GONE );
                        }
                    }
                }
        );
    }

    public void clearView()
    {
        imgvPicture.setImageDrawable( null );
        this.setOnClickListener( null );
    }
}
