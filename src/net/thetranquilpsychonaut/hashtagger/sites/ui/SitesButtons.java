package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
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

    public void show( Object result, boolean animate )
    {
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec( RelativeLayout.LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY );
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec( RelativeLayout.LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY );
        measure( widthMeasureSpec, heightMeasureSpec );
        int finalHeight = getMeasuredHeight();
        if ( animate )
        {
            final ValueAnimator animator = ValueAnimator.ofInt( 0, finalHeight );
            animator.setDuration( TIME_SCALE * finalHeight );
            animator.addListener( new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd( Animator animation )
                {
                    scrollIfHidden();
                }
            } );
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

    // If this view is hidden partially after show, we scroll the listview to make it fully visible
    private void scrollIfHidden()
    {
        // Get visible bounds of parent view, which is SitesListRow
        View parent = ( View ) getParent();
        Rect r = new Rect();
        parent.getLocalVisibleRect( r );
        int hiddenHeight = parent.getHeight() - r.height();
        // If view's height exceeds the visible bounds height, our view is partially hidden
        if ( hiddenHeight > 0 )
        {
            // Get reference to listview
            ListView lv = ( ListView ) parent.getParent();
            boolean isHiddenAtBottom = lv.getLastVisiblePosition() == lv.getPositionForView( parent );
            // We don't need to scroll is the view is hidden at the top
            // This suits us because smoothScrollBy method cannot scroll up anyway :P
            if ( isHiddenAtBottom )
            {
                lv.smoothScrollBy( hiddenHeight, 2 * hiddenHeight );
            }
        }
    }
}
