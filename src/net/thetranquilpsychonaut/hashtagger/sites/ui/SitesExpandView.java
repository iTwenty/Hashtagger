package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by itwenty on 4/30/14.
 */
public abstract class SitesExpandView extends RelativeLayout implements ValueAnimator.AnimatorUpdateListener
{
    private static final int TIME_SCALE = 2;
    private ValueAnimator animator;

    public SitesExpandView( Context context )
    {
        this( context, null, 0 );
    }

    public SitesExpandView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public SitesExpandView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        animator = new ValueAnimator();
        animator.addUpdateListener( this );
        animator.addListener( new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd( Animator animation )
            {
                scrollIfHidden();
            }
        } );
    }

    protected void expand( int height, boolean animate )
    {
        if ( animate )
        {
            animator.setIntValues( 0, height );
            animator.setDuration( height * TIME_SCALE );
            animator.start();
        }
        else
        {
            setHeight( height );
        }
    }

    protected void collapse( boolean animate )
    {
        int height = this.getHeight();
        if ( animate )
        {
            animator.setIntValues( height, 0 );
            animator.setDuration( height * TIME_SCALE );
            animator.start();
        }
        else
        {
            setHeight( 0 );
        }
    }

    @Override
    public void onAnimationUpdate( ValueAnimator animation )
    {
        setHeight( ( Integer ) animation.getAnimatedValue() );
    }


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

    // If bottom most view is hidden partially after expand/collapse, we scroll the listview to make it fully visible
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
                lv.smoothScrollBy( hiddenHeight, TIME_SCALE * hiddenHeight );
            }
        }
    }
}
