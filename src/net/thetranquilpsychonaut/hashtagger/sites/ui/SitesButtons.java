package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by itwenty on 5/2/14.
 */
public abstract class SitesButtons extends LinearLayout
{
    private static final int TIME_SCALE = 4;
    protected boolean isVisible;

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
        isVisible = false;
    }

    public void show( Object result, boolean animate, AnimatorListenerAdapter adapter )
    {
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec( RelativeLayout.LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY );
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec( RelativeLayout.LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY );
        measure( widthMeasureSpec, heightMeasureSpec );
        int finalHeight = getMeasuredHeight();
        if ( animate )
        {
            final ValueAnimator animator = ValueAnimator.ofInt( 0, finalHeight );
            animator.setDuration( TIME_SCALE * finalHeight );
            if ( null != adapter )
            {
                animator.addListener( adapter );
            }
            animator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate( ValueAnimator animation )
                {
                    setHeight( ( Integer ) animation.getAnimatedValue() );
                }
            } );
            animator.start();
        }
        else
        {
            setHeight( finalHeight );
        }
        isVisible = true;
        updateButtons( result );
    }

    protected abstract void updateButtons( Object result );

    public void hide( boolean animate )
    {
        if ( animate )
        {
            final ValueAnimator animator = ValueAnimator.ofInt( getHeight(), 0 );
            animator.setDuration( TIME_SCALE * getHeight() );
            animator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate( ValueAnimator animation )
                {
                    setHeight( ( Integer ) animation.getAnimatedValue() );
                }
            } );
            animator.start();
        }
        else
        {
            setHeight( 0 );
        }
        isVisible = false;
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
}
