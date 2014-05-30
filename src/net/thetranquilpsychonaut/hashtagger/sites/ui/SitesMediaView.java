package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/30/14.
 */
public abstract class SitesMediaView extends FrameLayout implements View.OnClickListener
{
    protected ImageView imgvMedia;
    protected ImageView imgvPlay;
    private int mediaWidth  = -1;
    private int mediaHeight = -1;

    public SitesMediaView( Context context )
    {
        this( context, null, 0 );
    }

    public SitesMediaView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public SitesMediaView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.sites_media_view, this );
        imgvMedia = ( ImageView ) findViewById( R.id.imgv_attachment );
        imgvPlay = ( ImageView ) findViewById( R.id.imgv_play );
        if ( null != attrs )
        {
            TypedArray typedArray = context.obtainStyledAttributes( attrs, R.styleable.SitesMediaView, defStyle, 0 );
            final int count = typedArray.getIndexCount();
            for ( int a = 0; a < count; ++a )
            {
                int attr = typedArray.getIndex( a );
                switch ( attr )
                {
                    case R.styleable.SitesMediaView_mediaWidth:
                        mediaWidth = typedArray.getDimensionPixelSize( attr, -1 );
                        break;
                    case R.styleable.SitesMediaView_mediaHeight:
                        mediaHeight = typedArray.getDimensionPixelSize( attr, -1 );
                        break;
                }
            }
            typedArray.recycle();
        }
        if ( mediaWidth == -1 )
        {
            mediaWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if ( mediaHeight == -1 )
        {
            mediaHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        ViewGroup.LayoutParams params = imgvMedia.getLayoutParams();
        params.width = mediaWidth;
        params.height = mediaHeight;
        imgvMedia.setLayoutParams( params );
        this.setOnClickListener( this );
    }

    public abstract void showMediaThumbnail( Object result );

    public abstract void showMedia( Object result );

    @Override
    public abstract void onClick( View v );
}
