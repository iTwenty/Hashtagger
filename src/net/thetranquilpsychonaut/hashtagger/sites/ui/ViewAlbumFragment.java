package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.widgets.TextDrawable;
import net.thetranquilpsychonaut.hashtagger.widgets.TouchImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 5/30/14.
 */
public class ViewAlbumFragment extends Fragment
{
    public static final String TAG                      = "ViewAlbumFragmentTag";
    public static final String ALBUM_THUMBNAIL_URLS_KEY = "thumbs";
    public static final String ALBUM_IMAGE_URLS_KEY     = "images";
    public static final String SELECTED_POSITION_KEY    = "pos";

    private ViewPager         albumPager;
    private List<String>      albumImageUrls;
    private int               selectedPosition;
    private AlbumPagerAdapter albumPagerAdapter;
    private TextDrawable      loading;
    private TextDrawable      error;

    public static ViewAlbumFragment newInstance( ArrayList<String> albumImageUrls, int selectedPosition )
    {
        ViewAlbumFragment f = new ViewAlbumFragment();
        Bundle args = new Bundle();
        args.putStringArrayList( ALBUM_IMAGE_URLS_KEY, albumImageUrls );
        args.putInt( SELECTED_POSITION_KEY, selectedPosition );
        f.setArguments( args );
        return f;
    }

    public static ViewAlbumFragment newInstance( Bundle args )
    {
        ViewAlbumFragment f = new ViewAlbumFragment();
        f.setArguments( args );
        return f;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        albumPager = ( ViewPager ) inflater.inflate( R.layout.fragment_view_album, container, false );
        albumImageUrls = getArguments().getStringArrayList( ALBUM_IMAGE_URLS_KEY );
        selectedPosition = getArguments().getInt( SELECTED_POSITION_KEY, 0 );
        albumPagerAdapter = new AlbumPagerAdapter();
        albumPager.setAdapter( albumPagerAdapter );
        albumPager.setCurrentItem( selectedPosition );
        loading = new TextDrawable( "Loading" );
        error = new TextDrawable( "Failed to load" );
        return albumPager;
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
        public Object instantiateItem( View container, int position )
        {
            TouchImageView touchImageView = new TouchImageView( container.getContext() );
            touchImageView.setLayoutParams( new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
            Picasso.with( container.getContext() )
                    .load( albumImageUrls.get( position ) )
                    .placeholder( loading )
                    .error( error )
                    .into( touchImageView );
            ( ( ViewPager ) container ).addView( touchImageView, 0 );
            return touchImageView;
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView( ( TouchImageView ) object );
        }
    }
}
