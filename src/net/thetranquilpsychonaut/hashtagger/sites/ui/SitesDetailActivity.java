package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.widgets.SlidingUpPanelLayout;
import net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator.IconPagerIndicator;

/**
 * Created by itwenty on 6/17/14.
 */
public abstract class SitesDetailActivity extends BaseActivity implements IconPagerIndicator.IconClickListener
{
    private SlidingUpPanelLayout slider;
    private FrameLayout          slidingView;
    private View                 mainView;
    private View                 dragView;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sites_detail );
        slider = ( SlidingUpPanelLayout ) findViewById( R.id.slider );
        slidingView = ( FrameLayout ) findViewById( R.id.sliding_view );
        mainView = initMainView( savedInstanceState );
        slider.addView( mainView, 0 );
        slider.setEnableDragViewTouchEvents( true );
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

    public View getSlidingView()
    {
        return slidingView;
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
