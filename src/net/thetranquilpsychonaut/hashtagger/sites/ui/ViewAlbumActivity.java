package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.widgets.TouchImageView;

import java.util.List;

/**
 * Created by itwenty on 5/29/14.
 */
public class ViewAlbumActivity extends BaseActivity
{
    public static final String ALBUM_IMAGE_URLS_KEY  = "urls";
    public static final String SELECTED_POSITION_KEY = "pos";

    private ViewPager         albumPager;
    private List<String>      imageUrls;
    private int               selectedPosition;
    private AlbumPagerAdapter albumPagerAdapter;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_album );
        if ( null == getIntent() )
            finish();
        imageUrls = getIntent().getStringArrayListExtra( ALBUM_IMAGE_URLS_KEY );
        selectedPosition = getIntent().getIntExtra( SELECTED_POSITION_KEY, 0 );
        if ( null == imageUrls || imageUrls.isEmpty() )
            finish();
        albumPager = ( ViewPager ) findViewById( R.id.album_pager );
        albumPagerAdapter = new AlbumPagerAdapter();
        albumPager.setAdapter( albumPagerAdapter );
        albumPager.setCurrentItem( selectedPosition );
    }

    private class AlbumPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return imageUrls.size();
        }

        @Override
        public boolean isViewFromObject( View view, Object o )
        {
            return view == o;
        }

        @Override
        public Object instantiateItem( View container, int position )
        {
            TouchImageView touchImageView = new TouchImageView( ViewAlbumActivity.this );
            touchImageView.setLayoutParams( new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
            Picasso.with( ViewAlbumActivity.this )
                    .load( imageUrls.get( position ) )
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