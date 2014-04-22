package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/18/14.
 */
public class FacebookObjectView extends RelativeLayout
{
    private ImageView imgvPicture;

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
        LayoutInflater.from( context ).inflate( R.layout.facebook_object_view, this );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
    }

    public void showObjectFromPost( Post post )
    {
        UrlImageViewHelper.setUrlDrawable( imgvPicture, post.getPicture().toString(), getResources().getDrawable( R.drawable.drawable_image_loading ), HashtaggerApp.CACHE_DURATION_MS );
    }
}
