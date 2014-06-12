package net.thetranquilpsychonaut.hashtagger.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/31/14.
 */
public class AlbumThumbnail extends FrameLayout
{
    private ImageView imgvAlbumThumbnail;
    private TextView  tvAlbumCount;

    public AlbumThumbnail( Context context )
    {
        this( context, null, 0 );
    }

    public AlbumThumbnail( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public AlbumThumbnail( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.album_thumbnail, this );
        imgvAlbumThumbnail = ( ImageView ) findViewById( R.id.imgv_album_thumbnail );
        tvAlbumCount = ( TextView ) findViewById( R.id.tv_album_count );
    }

    public ImageView getAlbumThumbnail()
    {
        return imgvAlbumThumbnail;
    }

    public TextView getAlbumCount()
    {
        return tvAlbumCount;
    }
}
