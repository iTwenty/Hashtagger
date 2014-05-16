package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.widgets.TouchImageView;

/**
 * Created by itwenty on 5/8/14.
 */
public class ViewImageActivity extends Activity
{
    public static final String IMAGE_URL_KEY = "image_url";

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_image );
        final TouchImageView touchImageView = ( TouchImageView ) findViewById( R.id.touch_image_view );
        Picasso.with( this ).load( getIntent().getStringExtra( IMAGE_URL_KEY ) ).into( touchImageView, new Callback()
        {
            @Override
            public void onSuccess()
            {
                findViewById( R.id.pgbr_touch_image_view_loading ).setVisibility( View.GONE );
                touchImageView.setVisibility( View.VISIBLE );
            }

            @Override
            public void onError()
            {
                findViewById( R.id.pgbr_touch_image_view_loading ).setVisibility( View.GONE );
                findViewById( R.id.tv_image_load_error ).setVisibility( View.VISIBLE );
            }
        } );
//        UrlImageViewHelper.setUrlDrawable(
//                touchImageView,
//                getIntent().getStringExtra( "image" ),
//                new UrlImageViewCallback()
//                {
//                    @Override
//                    public void onLoaded( ImageView imageView, Bitmap bitmap, String s, boolean b )
//                    {
//                        findViewById( R.id.pgbr_touch_image_view_loading ).setVisibility( View.GONE );
//                        touchImageView.setVisibility( View.VISIBLE );
//                        touchImageView.animate().alpha( 1 ).setDuration( 1000 ).start();
//                    }
//                }
//        );
    }
}