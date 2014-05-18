package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.widgets.TouchImageView;

import java.util.Calendar;

/**
 * Created by itwenty on 5/8/14.
 */
public class ViewImageActivity extends BaseActivity
{
    public static final String IMAGE_URL_KEY = "image_url";

    private boolean        imageLoaded;
    private TouchImageView touchImageView;
    private MyTarget       target;
    private Bitmap         imageBitmap;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_image );
        touchImageView = ( TouchImageView ) findViewById( R.id.touch_image_view );
        imageLoaded = false;
        target = new MyTarget();
        Picasso.with( HashtaggerApp.app ).load( getIntent().getStringExtra( IMAGE_URL_KEY ) ).into( target );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.activity_view_image_menu, menu );
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu( Menu menu )
    {
        menu.findItem( R.id.it_save_image ).setVisible( imageLoaded ? true : false );
        return true;
    }

    public void doSaveImage( MenuItem item )
    {
        if ( null != imageBitmap )
        {
            String imageName = String.valueOf( Calendar.getInstance().getTimeInMillis() );
            String savedImageUrl = MediaStore.Images.Media.insertImage( getContentResolver(), imageBitmap, imageName, "" );
            Toast.makeText( this, null == savedImageUrl ? "Failed to save image" : "Saved image to Gallery", Toast.LENGTH_SHORT ).show();
        }
    }

    private class MyTarget implements Target
    {
        @Override
        public void onBitmapLoaded( Bitmap bitmap, Picasso.LoadedFrom loadedFrom )
        {
            findViewById( R.id.pgbr_touch_image_view_loading ).setVisibility( View.GONE );
            touchImageView.setImageBitmap( bitmap );
            touchImageView.setVisibility( View.VISIBLE );
            touchImageView.animate().alpha( 1f ).setDuration( 1000 ).start();
            imageLoaded = true;
            invalidateOptionsMenu();
            imageBitmap = bitmap;
        }

        @Override
        public void onBitmapFailed( Drawable drawable )
        {
            touchImageView.setVisibility( View.GONE );
            touchImageView.setAlpha( 0f );
            findViewById( R.id.pgbr_touch_image_view_loading ).setVisibility( View.GONE );
            findViewById( R.id.tv_image_load_error ).setVisibility( View.VISIBLE );
        }

        @Override
        public void onPrepareLoad( Drawable drawable )
        {

        }
    }
}