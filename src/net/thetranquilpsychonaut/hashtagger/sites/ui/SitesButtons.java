package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.widgets.CenterContentButton;

/**
 * Created by itwenty on 5/2/14.
 */
public abstract class SitesButtons extends LinearLayout
{
    private static final int TIME_SCALE = 4;

    protected boolean mIsVisible;
    protected boolean mShowSmallButtons;
    protected boolean mShowViewDetailsButton;

    private int mExpandedHeight = 0;
    private ValueAnimator mHeightAnimator;

    public SitesButtons( Context context )
    {
        this( context, null, 0 );
    }

    public SitesButtons( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public SitesButtons( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        if ( null != attrs )
        {
            TypedArray ta = context.obtainStyledAttributes( attrs, R.styleable.SitesButtons );
            mShowSmallButtons = ta.getBoolean( R.styleable.SitesButtons_showSmallButtons, false );
            mShowViewDetailsButton = ta.getBoolean( R.styleable.SitesButtons_showViewDetailsButton, false );
        }
        mIsVisible = false;
        mHeightAnimator = new ValueAnimator();
        mHeightAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate( ValueAnimator animation )
            {
                setHeight( ( Integer ) animation.getAnimatedValue() );
            }
        } );
    }

    public void show( Object result, boolean animate, AnimatorListenerAdapter adapter )
    {
        if ( animate )
        {
            int expandedWidthMeasureSpec = MeasureSpec.makeMeasureSpec( RelativeLayout.LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY );
            int expandedHeightMeasureSpec = MeasureSpec.makeMeasureSpec( RelativeLayout.LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY );
            measure( expandedWidthMeasureSpec, expandedHeightMeasureSpec );
            mExpandedHeight = getMeasuredHeight();
            mHeightAnimator.setDuration( TIME_SCALE * mExpandedHeight );
            mHeightAnimator.removeAllListeners();
            mHeightAnimator.setIntValues( 0, mExpandedHeight );
            if ( null != adapter )
            {
                mHeightAnimator.addListener( adapter );
            }
            mHeightAnimator.start();
        }
        else
        {
            setHeight( mExpandedHeight );
        }
        mIsVisible = true;
        updateButtons( result );
    }

    public abstract void updateButtons( Object result );

    public void hide( boolean animate )
    {
        if ( animate )
        {
            mHeightAnimator.removeAllListeners();
            mHeightAnimator.setIntValues( mExpandedHeight, 0 );
            mHeightAnimator.start();
        }
        else
        {
            setHeight( 0 );
        }
        mIsVisible = false;
        clearButtons();

    }

    protected abstract void clearButtons();

    private void setHeight( int height )
    {
        if ( height == this.getHeight() )
        {
            return;
        }
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.height = height;
        this.setLayoutParams( params );
        this.requestLayout();
    }

    protected void setCenterDrawable( CenterContentButton ccb, int drawableResId )
    {
        ccb.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable( drawableResId ),
                null,
                null,
                null );
    }
}
