package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.TwoWayView;

import java.util.ArrayList;

/**
 * Created by itwenty on 5/29/14.
 */
public class GPlusAlbumRow extends SitesListRow implements AdapterView.OnItemClickListener
{
    public static final String ALBUM_IMAGE_URLS = "ALBUM_IMAGE_URLS";

    private GPlusHeader       gPlusHeader;
    private TextView          tvMessage;
    private TwoWayView        albumView;
    private Activity          activity;
    private ArrayList<String> albumImageUrls;
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
        gPlusHeader = ( GPlusHeader ) findViewById( R.id.gplus_header );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        albumView = ( TwoWayView ) findViewById( R.id.album_view );
        albumImageUrls = new ArrayList<String>( 50 );
        albumAdapter = new GPlusAlbumAdapter( context, 0, albumImageUrls );
        albumView.setAdapter( albumAdapter );
        albumView.setOnItemClickListener( this );
        super.init( context );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.gplus_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        this.activity = ( Activity ) result;
        gPlusHeader.updateHeader( this.activity );
        tvMessage.setText( activity.getObject().getOriginalContent() );
        albumImageUrls.clear();
        albumImageUrls.addAll( ( ArrayList<String> ) activity.get( ALBUM_IMAGE_URLS ) );
        albumAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        String imageUrl = ( String ) parent.getItemAtPosition( position );
        Intent i = new Intent( getContext(), ViewImageActivity.class );
        i.putExtra( ViewImageActivity.IMAGE_URL_KEY, Helper.getGPlusLargeMediaUrl( imageUrl ) );
        getContext().startActivity( i );
    }
}
