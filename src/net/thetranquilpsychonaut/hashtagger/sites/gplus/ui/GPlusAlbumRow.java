package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.*;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.ArrayList;

/**
 * Created by itwenty on 5/29/14.
 */
public class GPlusAlbumRow extends GPlusListRow implements View.OnClickListener
{
    private AlbumThumbnail albumThumbnail;
    private int            albumCount;

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
        albumThumbnail = ( AlbumThumbnail ) findViewById( R.id.album_thumbnail );
        albumThumbnail.setOnClickListener( this );
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
        albumCount = activity.getObject().getAttachments().get( 0 ).getThumbnails().size();
        Picasso.with( getContext() )
                .load( activity.getObject().getAttachments().get( 0 ).getThumbnails().get( 0 ).getImage().getUrl() )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( albumThumbnail.getAlbumThumbnail() );
        if ( albumCount == 1 )
        {
            albumThumbnail.getAlbumCount().setVisibility( GONE );
        }
        else
        {
            albumThumbnail.getAlbumCount().setText( albumCount + " images" );
            albumThumbnail.getAlbumCount().setVisibility( VISIBLE );
        }
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( albumThumbnail ) )
        {
            if ( albumCount == 1 )
            {
                String imageUrl = Helper.getGPlusAlbumImageUrl( activity.getObject().getAttachments().get( 0 ).getThumbnails().get( 0 ).getImage().getUrl() );
                ViewAlbumActivity.createAndStartActivity( v.getContext(), Helper.createStringArrayList( imageUrl ), 0 );
            }
            else
            {
                ArrayList<String> albumThumbnailUrls = new ArrayList<String>( activity.getObject().getAttachments().get( 0 ).getThumbnails().size() );
                for ( Activity.PlusObject.Attachments.Thumbnails thumbnail : activity.getObject().getAttachments().get( 0 ).getThumbnails() )
                {
                    albumThumbnailUrls.add( thumbnail.getImage().getUrl() );
                }
                ViewAlbumThumbnailsFragment fragment = ViewAlbumThumbnailsFragment.newInstance( albumThumbnailUrls, false, HashtaggerApp.GPLUS_VALUE );
                fragment.show( ( ( SitesActivity ) getContext() ).getSupportFragmentManager(), ViewAlbumThumbnailsFragment.TAG );
            }
        }
    }
}