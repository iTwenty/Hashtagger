package net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class IconPagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener, View.OnClickListener
{
    public static interface IconClickListener
    {
        public void onIconClicked( int position );
    }

    private static final int DEFAULT_PADDING = Helper.convertPxToDp( 5 );

    private ViewPager         mViewPager;
    private IconPagerAdapter  mAdapter;
    private IconClickListener listener;

    public IconPagerIndicator( Context context )
    {
        this( context, null, 0 );
    }

    public IconPagerIndicator( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public IconPagerIndicator( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    public void setViewPager( ViewPager view )
    {
        if ( view == mViewPager )
        {
            return;
        }
        if ( null != mViewPager )
        {
            mViewPager.setOnPageChangeListener( null );
        }
        PagerAdapter adapter = view.getAdapter();
        if ( null == adapter )
        {
            throw new RuntimeException( "ViewPager does not have an adapter set!" );
        }
        if ( !( adapter instanceof IconPagerAdapter ) )
        {
            throw new RuntimeException( "ViewPager's adapter does not implement IconPagerAdapter!" );
        }
        mViewPager = view;
        mAdapter = ( IconPagerAdapter ) adapter;
        view.setOnPageChangeListener( this );
        notifyIconSetChanged();
    }


    public void notifyIconSetChanged()
    {
        this.removeAllViews();
        int count = mAdapter.getCount();
        for ( int i = 0; i < count; ++i )
        {
            ImageButton view = new ImageButton( getContext(), null, android.R.style.Widget_Holo_Button_Borderless_Small );
            LinearLayout.LayoutParams params = new LayoutParams( 0, LayoutParams.WRAP_CONTENT, 1 );
            view.setPadding( DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING );
            params.gravity = Gravity.CENTER;
            view.setLayoutParams( params );
            view.setImageResource( mAdapter.getIconResId( i ) );
            view.setOnClickListener( this );
            addView( view );
        }
        setSelectedChild( mViewPager.getCurrentItem() );
        requestLayout();
    }

    public ViewPager getViewPager()
    {
        if ( null == mViewPager )
        {
            throw new RuntimeException( "ViewPager not set!" );
        }
        return mViewPager;
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

    public void setSelectedChild( int position )
    {
        if ( null == mViewPager )
        {
            throw new RuntimeException( "ViewPager not yet been set!" );
        }
        if ( null == mAdapter )
        {
            throw new RuntimeException( "ViewPager's adapter has not yet been set!" );
        }
        ImageButton child;
        int childCount = getChildCount();
        for ( int a = 0; a < childCount; ++a )
        {
            child = ( ImageButton ) getChildAt( a );
            if ( a == position )
            {
                child.setColorFilter( getResources().getColor( mAdapter.getSelectedColor( position ) ) );
            }
            else
            {
                child.setColorFilter( null );
            }
        }
    }

    public void setIconsClickable( boolean iconsClickable )
    {
        int childCount = getChildCount();
        for ( int i = 0; i < childCount; ++i )
        {
            getChildAt( i ).setEnabled( iconsClickable );
        }
    }

    public void setIconClickListener( IconClickListener listener )
    {
        this.listener = listener;
    }

    public void removeIconClickListener()
    {
        this.listener = null;
    }

    @Override
    public void onClick( View v )
    {
        int clickedPosition = indexOfChild( v );
        mViewPager.setCurrentItem( clickedPosition );
        if ( null != listener )
        {
            listener.onIconClicked( clickedPosition );
        }
    }
}
