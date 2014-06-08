package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.SingleMediaScanner;
import net.thetranquilpsychonaut.hashtagger.widgets.TextDrawable;
import net.thetranquilpsychonaut.hashtagger.widgets.TouchImageView;
import org.w3c.dom.Text;

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
    public static final String USERNAME_KEY          = "usrnm";
    public static final String ALBUM_IMAGE_URLS_KEY  = "images";
    public static final String SELECTED_POSITION_KEY = "pos";

    // Flags indicating what toast message to show on saving image
    private static final int SAVE_IMAGE_FAILED     = 0;
    private static final int SAVE_IMAGE_SUCCEEDED  = 1;
    private static final int SAVE_IMAGE_NOT_LOADED = 2;

    private ViewPager         albumPager;
    private List<String>      albumImageUrls;
    private int               selectedPosition;
    private AlbumPagerAdapter albumPagerAdapter;
    private TextDrawable      loading;
    private TextDrawable      error;

    public static void createAndStartActivity( Context context, String userName, ArrayList<String> albumImageUrls, int selectedPosition )
    {
        Intent i = new Intent( context, ViewAlbumActivity.class );
        i.putExtra( USERNAME_KEY, userName );
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
        String userName = getIntent().getStringExtra( USERNAME_KEY );
        setTitle( TextUtils.isEmpty( userName ) ?
                getResources().getString( R.string.app_name ) :
                userName + "'s photo" + ( albumImageUrls.size() == 1 ? "" : "s" ) );
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
            touchImageView.setTag( position );
            touchImageView.setLayoutParams( new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
            Picasso.with( container.getContext() )
                    .load( albumImageUrls.get( position ) )
                    .placeholder( loading )
                    .error( error )
                    .into( touchImageView );
            container.addView( touchImageView, 0 );
            return touchImageView;
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView( ( TouchImageView ) object );
        }
    }

    public void doSaveImage( MenuItem item )
    {
        TouchImageView currentView = ( TouchImageView ) albumPager.findViewWithTag( albumPager.getCurrentItem() );

        if ( null == currentView )
        {
            showSaveImageToast( SAVE_IMAGE_FAILED );
            return;
        }
        Drawable drawable = currentView.getDrawable();
        if ( null == drawable || error.equals( drawable ) )
        {
            showSaveImageToast( SAVE_IMAGE_FAILED );
            return;
        }
        if ( loading.equals( drawable ) )
        {
            showSaveImageToast( SAVE_IMAGE_NOT_LOADED );
            return;
        }
        Picasso.with( this ).load( albumImageUrls.get( albumPager.getCurrentItem() ) ).into( new Target()
        {
            @Override
            public void onBitmapLoaded( Bitmap bitmap, Picasso.LoadedFrom loadedFrom )
            {
                Bitmap newBitmap = bitmap.copy( bitmap.getConfig(), true );
                if ( null == newBitmap )
                {
                    showSaveImageToast( SAVE_IMAGE_FAILED );
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
                    new SingleMediaScanner( ViewAlbumActivity.this, imageFile );
                    success = true;
                }
                catch ( FileNotFoundException e )
                {
                    Helper.debug( String.format( "Cannot open image file : %s", imageFile.getAbsolutePath() ) );
                }
                showSaveImageToast( success ? SAVE_IMAGE_SUCCEEDED : SAVE_IMAGE_FAILED );
            }

            @Override
            public void onBitmapFailed( Drawable drawable )
            {
                showSaveImageToast( SAVE_IMAGE_FAILED );
            }

            @Override
            public void onPrepareLoad( Drawable drawable )
            {

            }
        } );
    }

    private void showSaveImageToast( int code )
    {
        String message;
        switch ( code )
        {
            case SAVE_IMAGE_SUCCEEDED:
                message = "Image saved";
                break;
            case SAVE_IMAGE_NOT_LOADED:
                message = "Image not loaded yet";
                break;
            case SAVE_IMAGE_FAILED: // Fall through
            default:
                message = "Failed to save image";
                break;
        }
        Toast.makeText( this, message, Toast.LENGTH_SHORT ).show();
    }
}