package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import net.thetranquilpsychonaut.hashtagger.R;

import java.util.ArrayList;

/**
 * Created by itwenty on 5/29/14.
 */
public class ViewAlbumActivity extends BaseActivity
{
    public static void createAndStartActivity( Context context, ArrayList<String> albumImageUrls, int selectedPosition )
    {
        Intent i = new Intent( context, ViewAlbumActivity.class );
        Bundle b = new Bundle();
        b.putStringArrayList( ViewAlbumFragment.ALBUM_IMAGE_URLS_KEY, albumImageUrls );
        b.putInt( ViewAlbumFragment.SELECTED_POSITION_KEY, selectedPosition );
        i.putExtras( b );
        context.startActivity( i );
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_album );
        if ( null == getIntent() || null == getIntent().getExtras() )
        {
            finish();
        }
        if ( null == savedInstanceState )
        {
            getSupportFragmentManager().beginTransaction()
                    .add( R.id.album_holder, ViewAlbumFragment.newInstance( getIntent().getExtras() ), ViewAlbumFragment.TAG )
                    .commit();
        }
    }
}