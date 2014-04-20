package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by itwenty on 4/20/14.
 */
public class ViewExpander implements ValueAnimator.AnimatorUpdateListener
{
    private static final int TIME_SCALE = 2;
    private View          view;
    private ValueAnimator animator;

    public ViewExpander( View view )
    {
        this.view = view;
        animator = new ValueAnimator();
        animator.addUpdateListener( this );
    }

    public void expandView( int height, boolean animate )
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

    public void collapseView( boolean animate )
    {
        if ( animate )
        {
            animator.setIntValues( view.getHeight(), 0 );
            animator.setDuration( view.getHeight() * TIME_SCALE );
            animator.start();
        }
        else
        {
            setHeight( 0 );
        }
    }

    private void setHeight( int height )
    {
        if ( height == view.getHeight() )
            return;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams( params );
        view.requestLayout();
    }

    @Override
    public void onAnimationUpdate( ValueAnimator animation )
    {
        setHeight( ( Integer ) animation.getAnimatedValue() );
    }
}
