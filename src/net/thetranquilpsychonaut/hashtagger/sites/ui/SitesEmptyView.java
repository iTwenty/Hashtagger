package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/28/14.
 */
public class SitesEmptyView extends LinearLayout
{
    private ImageView imgvEmpty;
    private TextView  tvEmpty;

    public SitesEmptyView( Context context )
    {
        this( context, null, 0 );
    }

    public SitesEmptyView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public SitesEmptyView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.sites_empty_view, this );
        this.setOrientation( LinearLayout.VERTICAL );
        imgvEmpty = ( ImageView ) findViewById( R.id.imgv_empty );
        tvEmpty = ( TextView ) findViewById( R.id.tv_empty );
    }

    public void setText( CharSequence text )
    {
        tvEmpty.setText( text );
    }

    public void setText( String text )
    {
        tvEmpty.setText( text );
    }

    public void setImage( int imageResId )
    {
        imgvEmpty.setImageDrawable( getResources().getDrawable( imageResId ) );
    }
}
