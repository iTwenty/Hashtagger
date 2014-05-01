package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

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
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterNormalExpandRow extends RelativeLayout
{
    TwitterButtons twitterButtons;

    public TwitterNormalExpandRow( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterNormalExpandRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterNormalExpandRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.twitter_buttons, this );
        twitterButtons = ( TwitterButtons ) findViewById( R.id.twitter_buttons );
    }

    public void expand( boolean animate )
    {
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec( LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY );
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec( LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY );
        measure( widthMeasureSpec, heightMeasureSpec );
        int finalHeight = getMeasuredHeight();
        if ( animate )
        {
            final ValueAnimator animator = ValueAnimator.ofInt( 0, finalHeight );
            animator.setDuration( 2 * finalHeight );
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
    }

    public void collapse( boolean animate )
    {
        if ( animate )
        {
            final ValueAnimator animator = ValueAnimator.ofInt( getHeight(), 0 );
            animator.setDuration( 2 * getHeight() );
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

    // If this view is hidden partially after expand, we scroll the listview to make it fully visible
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
