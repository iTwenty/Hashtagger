package net.thetranquilpsychonaut.hashtagger.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 6/12/14.
 */
public class VideoThumbnail extends FrameLayout
{
    private ImageView imgvVideoThumbnail;
    private ImageView imgvPlay;

    public VideoThumbnail( Context context )
    {
        this( context, null, 0 );
    }

    public VideoThumbnail( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public VideoThumbnail( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.video_thumbnail, this );
        imgvVideoThumbnail = ( ImageView ) findViewById( R.id.imgv_video_thumbnail );
        imgvPlay = ( ImageView ) findViewById( R.id.imgv_play );
    }

    public ImageView getVideoThumbnail()
    {
        return imgvVideoThumbnail;
    }

    public void showPlayButton( boolean show )
    {
        imgvPlay.setVisibility( show ? VISIBLE : GONE );
    }
}
