package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumFragment;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.TwoWayView;

import java.util.ArrayList;

/**
 * Created by itwenty on 5/29/14.
 */
public class GPlusAlbumRow extends GPlusListRow implements AdapterView.OnItemClickListener
{
    private TwoWayView        albumView;
    private ArrayList<String> albumThumbnailUrls;
    private GPlusAlbumAdapter albumAdapter;

    protected GPlusAlbumRow( Context context )
    {
        super( context );
    }

    protected GPlusAlbumRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected GPlusAlbumRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.gplus_album_row, this );
        albumView = ( TwoWayView ) findViewById( R.id.album_view );
        albumThumbnailUrls = new ArrayList<String>();
        albumAdapter = new GPlusAlbumAdapter( context, 0, albumThumbnailUrls );
        albumView.setAdapter( albumAdapter );
        albumView.setOnItemClickListener( this );
        super.init( context );
    }

    @Override
    protected GPlusHeader initGPlusHeader()
    {
        return ( GPlusHeader ) findViewById( R.id.gplus_header );
    }

    @Override
    protected TextView initActivityText()
    {
        return ( TextView ) findViewById( R.id.tv_activity_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.gplus_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        albumThumbnailUrls.clear();
        albumThumbnailUrls.addAll( ( ArrayList<String> ) activity.get( ViewAlbumFragment.ALBUM_THUMBNAIL_URLS_KEY ) );
        albumAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        ArrayList<String> albumImageUrls = new ArrayList<String>( albumThumbnailUrls.size() );
        for ( String url : albumThumbnailUrls )
        {
            albumImageUrls.add( Helper.getGPlusLargeImageUrl( url ) );
        }
        ViewAlbumActivity.createAndStartActivity( getContext(), albumImageUrls, position );
    }
}
