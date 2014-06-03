package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.SingleMediaScanner;
import net.thetranquilpsychonaut.hashtagger.widgets.TextDrawable;
import net.thetranquilpsychonaut.hashtagger.widgets.TouchImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by itwenty on 5/29/14.
 */
public class ViewAlbumActivity extends BaseActivity
{
    public static final String ALBUM_IMAGE_URLS_KEY  = "images";
    public static final String SELECTED_POSITION_KEY = "pos";

    private ViewPager         albumPager;
    private List<String>      albumImageUrls;
    private int               selectedPosition;
    private AlbumPagerAdapter albumPagerAdapter;
    private TextDrawable      loading;
    private TextDrawable      error;
    private Bitmap            currentBitmap;

    public static void createAndStartActivity( Context context, ArrayList<String> albumImageUrls, int selectedPosition )
    {
        Intent i = new Intent( context, ViewAlbumActivity.class );
        i.putStringArrayListExtra( ALBUM_IMAGE_URLS_KEY, albumImageUrls );
        i.putExtra( SELECTED_POSITION_KEY, selectedPosition );
        context.startActivity( i );
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_album );

        albumPager = ( ViewPager ) findViewById( R.id.album_pager );
        if ( null == getIntent() )
        {
            finish();
        }
        albumImageUrls = getIntent().getStringArrayListExtra( ALBUM_IMAGE_URLS_KEY );
        if ( null == albumImageUrls || albumImageUrls.size() == 0 )
        {
            finish();
        }
        selectedPosition = getIntent().getIntExtra( SELECTED_POSITION_KEY, 0 );
        albumPagerAdapter = new AlbumPagerAdapter();
        albumPager.setAdapter( albumPagerAdapter );
        albumPager.setCurrentItem( selectedPosition );
        loading = new TextDrawable( "Loading" );
        error = new TextDrawable( "Failed to load" );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.activity_view_album_menu, menu );
        return true;
    }

    private class AlbumPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return albumImageUrls.size();
        }

        @Override
        public boolean isViewFromObject( View view, Object o )
        {
            return view == o;
        }

        @Override
        public Object instantiateItem( ViewGroup container, int position )
        {
            TouchImageView touchImageView = new TouchImageView( container.getContext() );
            // We create a new Target and load our fetched image into it rather than the imageview directly
            // because we first need access to the raw bitmap to allow users to save it
            MyTarget target = new MyTarget( touchImageView );
            touchImageView.setLayoutParams( new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
            Picasso.with( container.getContext() )
                    .load( albumImageUrls.get( position ) )
                    .into( target );
            container.addView( touchImageView, 0 );
            return touchImageView;
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView( ( TouchImageView ) object );
        }

        private class MyTarget implements Target
        {
            TouchImageView view;

            public MyTarget( TouchImageView view )
            {
                this.view = view;
            }

            @Override
            public void onBitmapLoaded( Bitmap bitmap, Picasso.LoadedFrom loadedFrom )
            {
                ViewAlbumActivity.this.currentBitmap = bitmap;
                view.setImageBitmap( bitmap );
            }

            @Override
            public void onBitmapFailed( Drawable drawable )
            {
                view.setImageDrawable( error );
            }

            @Override
            public void onPrepareLoad( Drawable drawable )
            {
                view.setImageDrawable( loading );
            }
        }
    }

    public void doSaveImage( MenuItem item )
    {
        if ( null == currentBitmap )
        {
            Toast.makeText( this, "Image not loaded yet.", Toast.LENGTH_SHORT ).show();
            return;
        }
        Bitmap newBitmap = currentBitmap.copy( currentBitmap.getConfig(), true );
        if ( null == newBitmap )
        {
            Toast.makeText( this, "Failed to saved image", Toast.LENGTH_SHORT ).show();
            return;
        }
        boolean success = false;
        String fileName = String.valueOf( Calendar.getInstance().getTimeInMillis() ) + ".png";
        File fileDir = new File( Environment.getExternalStorageDirectory() + "/Hashtagger" );
        if ( !fileDir.exists() )
        {
            fileDir.mkdirs();
        }
        File imageFile = new File( fileDir, fileName );
        try
        {
            OutputStream out = new FileOutputStream( imageFile );
            newBitmap.compress( Bitmap.CompressFormat.PNG, 100, out );
            new SingleMediaScanner( this, imageFile );
            success = true;
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        Toast.makeText( this, success ? "Image saved" : "Failed to saved image", Toast.LENGTH_SHORT ).show();
    }
}