package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 6/3/14.
 */
public abstract class ViewPagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener
{
    private ViewPager vpViewPager;
    private int       selectedChildColorId;

    public ViewPagerIndicator( Context context )
    {
        this( context, null, 0 );
    }

    public ViewPagerIndicator( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public ViewPagerIndicator( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        selectedChildColorId = R.color.orange;
    }

    public void setViewPager( ViewPager view )
    {
        if ( vpViewPager != null )
        {
            vpViewPager.setOnPageChangeListener( null );
        }
        vpViewPager = view;
        view.setOnPageChangeListener( this );
        setSelectedChild( vpViewPager.getCurrentItem() );
    }

    public ViewPager getViewPager()
    {
        if ( null == vpViewPager )
        {
            throw new RuntimeException( "ViewPager not set!" );
        }
        return vpViewPager;
    }

    @Override
    public void onPageScrolled( int i, float v, int i2 )
    {

    }

    @Override
    public void onPageSelected( int i )
    {
        setSelectedChild( i );
    }

    @Override
    public void onPageScrollStateChanged( int i )
    {

    }

    protected void setSelectedChild( int position )
    {
        ImageButton child;
        int childCount = getChildCount();
        for ( int a = 0; a < childCount; ++a )
        {
            child = ( ImageButton ) getChildAt( a );
            if ( a == position )
            {
                child.setColorFilter( getResources().getColor(
                        null == child.getTag() ?
                                selectedChildColorId :
                                ( Integer ) child.getTag() ) );
            }
            else
            {
                child.setColorFilter( null );
            }
        }
    }
}
