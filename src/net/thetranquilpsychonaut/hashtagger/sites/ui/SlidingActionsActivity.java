package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.SlidingUpPanelLayout;
import net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator.IconPagerIndicator;

/**
 * Created by itwenty on 6/17/14.
 * <p/>
 * Activity with SlidingUpPanelLayout as it's root layout.
 * Provides a FrameLayout for subclasses to put the sliding view in
 * and initMainView() method to get the main view. getDragView() can be
 * used to provide a drag view for the sliding panel. If the provided
 * dragView is an IconPagerIndicator, then clicking an icon also brings
 * up the panel
 */
public abstract class SlidingActionsActivity extends BaseActivity implements IconPagerIndicator.IconClickListener
{
    private SlidingUpPanelLayout slider;
    private FrameLayout          slidingViewContainer;
    private View                 mainView;
    private View                 dragView;
    private int                  mPanelHeight;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sites_detail );
        slider = ( SlidingUpPanelLayout ) findViewById( R.id.slider );
        slidingViewContainer = ( FrameLayout ) findViewById( R.id.sliding_view );
        mainView = initMainView( savedInstanceState );
        slider.addView( mainView, 0 );
        slider.setEnableDragViewTouchEvents( true );
        // We want only the draggable view to shown at the bottom.
        // Ensuring that the draggable view is an ImageButton with
        // image of size 32x32, and knowing that android is going to
        // request one size higher at runtime, we can safely set the
        // height to a fixed value of 48dp.
        mPanelHeight = Helper.convertDpToPx( 48 );
        slider.setPanelHeight( mPanelHeight );
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        dragView = getDragView();
        slider.setDragView( dragView );
        if ( dragView instanceof IconPagerIndicator )
        {
            ( ( IconPagerIndicator ) dragView ).setIconClickListener( this );
        }
    }

    protected abstract View initMainView( Bundle savedInstanceState );

    protected abstract View getDragView();

    @Override
    public void onBackPressed()
    {
        if ( slider.isPanelExpanded() )
        {
            slider.collapsePanel();
            return;
        }
        super.onBackPressed();
    }

    public FrameLayout getSlidingViewContainer()
    {
        return slidingViewContainer;
    }

    @Override
    public void onIconClicked( int position )
    {
        if ( !slider.isPanelExpanded() )
        {
            slider.expandPanel();
        }
    }
}
