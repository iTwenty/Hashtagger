package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 6/3/14.
 */
public class NavDrawerPagerIndicator extends ViewPagerIndicator implements View.OnClickListener
{
    private ImageButton imgbTrending;
    private ImageButton imgbSaved;

    public NavDrawerPagerIndicator( Context context )
    {
        this( context, null, 0 );
    }

    public NavDrawerPagerIndicator( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public NavDrawerPagerIndicator( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.nav_drawer_pager_indicator, this );
        imgbTrending = ( ImageButton ) findViewById( R.id.tv_trending );
        imgbSaved = ( ImageButton ) findViewById( R.id.tv_saved );
        imgbTrending.setOnClickListener( this );
        imgbSaved.setOnClickListener( this );
    }

    @Override
    public void onClick( View v )
    {
        getViewPager().setCurrentItem( indexOfChild( v ) );
    }
}
