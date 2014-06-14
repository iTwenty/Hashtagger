package net.thetranquilpsychonaut.hashtagger.sites.ui;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.TwoWayView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 5/31/14.
 */
public class ViewAlbumThumbnailsFragment extends DialogFragment implements AdapterView.OnItemClickListener
{
    public static final String TAG                      = "ViewAlbumThumbnailsFragment";
    public static final String USERNAME_KEY             = "usrnm";
    public static final String ALBUM_THUMBNAIL_URLS_KEY = "urls";
    public static final String EMBED_KEY                = "embed";
    public static final String SITE_VALUE_KEY           = "sval";

    private AdapterView            albumThumbnailsView;
    private String                 userName;
    private List<String>           albumThumbnailUrls;
    private AlbumThumbnailsAdapter albumThumbnailsAdapter;
    private boolean                isEmbedded;
    private int                    siteValue;

    public static ViewAlbumThumbnailsFragment newInstance( String userName,
                                                           ArrayList<String> albumThumbnailUrls,
                                                           boolean embed,
                                                           int siteValue )
    {
        ViewAlbumThumbnailsFragment f = new ViewAlbumThumbnailsFragment();
        Bundle b = new Bundle();
        b.putString( USERNAME_KEY, userName );
        b.putStringArrayList( ALBUM_THUMBNAIL_URLS_KEY, albumThumbnailUrls );
        b.putBoolean( EMBED_KEY, embed );
        b.putInt( SITE_VALUE_KEY, siteValue );
        f.setArguments( b );
        return f;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        albumThumbnailUrls = getArguments().getStringArrayList( ALBUM_THUMBNAIL_URLS_KEY );
        if ( null == albumThumbnailUrls || albumThumbnailUrls.size() == 0 )
        {
            dismiss();
        }
        userName = getArguments().getString( USERNAME_KEY );
        isEmbedded = getArguments().getBoolean( EMBED_KEY );
        siteValue = getArguments().getInt( SITE_VALUE_KEY );
        setStyle( STYLE_NO_FRAME, 0 );
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        Dialog d = super.onCreateDialog( savedInstanceState );
        if ( !isEmbedded )
        {
            d.getWindow().setWindowAnimations( android.R.style.Animation_Dialog );
            d.setCanceledOnTouchOutside( true );
        }
        return d;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        if ( isEmbedded )
        {
            albumThumbnailsView = ( TwoWayView ) inflater.inflate(
                    R.layout.fragment_album_thumbnails_strip,
                    container,
                    false );
            ( ( TwoWayView ) albumThumbnailsView ).setItemMargin( Helper.convertDpToPx( 10 ) );
        }
        else
        {
            albumThumbnailsView = ( GridView ) inflater.inflate(
                    R.layout.fragment_album_thumbnails_grid,
                    container,
                    false );
        }
        albumThumbnailsView.setOnItemClickListener( this );
        return albumThumbnailsView;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState )
    {
        super.onViewCreated( view, savedInstanceState );
        albumThumbnailsAdapter = new AlbumThumbnailsAdapter( view.getContext(), albumThumbnailUrls );
        albumThumbnailsView.setAdapter( albumThumbnailsAdapter );
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        switch ( siteValue )
        {
            case HashtaggerApp.TWITTER_VALUE:
                break;
            case HashtaggerApp.FACEBOOK_VALUE:
                break;
            case HashtaggerApp.GPLUS_VALUE:
                ArrayList<String> albumImageUrls = new ArrayList<String>( albumThumbnailUrls.size() );
                for ( String thumbnailUrl : albumThumbnailUrls )
                {
                    albumImageUrls.add( UrlModifier.getGPlusFullAlbumImageUrl( thumbnailUrl ) );
                }
                ViewAlbumActivity.createAndStartActivity( getActivity(), userName, albumImageUrls, position );
                break;
        }
    }

    private static class AlbumThumbnailsAdapter extends BaseAdapter
    {
        private Context      context;
        private List<String> albumThumbnailUrls;

        public AlbumThumbnailsAdapter( Context context, List<String> albumThumbnailUrls )
        {
            this.context = context;
            this.albumThumbnailUrls = albumThumbnailUrls;
        }

        @Override
        public int getCount()
        {
            return albumThumbnailUrls.size();
        }

        @Override
        public Object getItem( int position )
        {
            return albumThumbnailUrls.get( position );
        }

        @Override
        public long getItemId( int position )
        {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            ImageView imageView;

            if ( null == convertView )
            {
                imageView = ( ImageView ) LayoutInflater.from( context ).inflate(
                        R.layout.album_thumbnails_cell,
                        parent,
                        false );
            }
            else
            {
                imageView = ( ImageView ) convertView;
            }

            Picasso.with( context )
                    .load( albumThumbnailUrls.get( position ) )
                    .fit()
                    .centerCrop()
                    .into( imageView );

            return imageView;
        }
    }
}
