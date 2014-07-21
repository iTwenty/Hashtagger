package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ViewAnimator;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.widgets.SlidingUpPanelLayout;

/**
 * Created by itwenty on 7/20/14.
 * Detail activity with a bottom panel containing action buttons. Sliding up from this panel shows
 * the relevant SitesActionsFragment
 */
public abstract class SlidingUpPanelDetailActivity extends BaseActivity
{
    private static final String ACTIVE_VIEW = "av";

    private SlidingUpPanelLayout slider;
    private ViewAnimator         vaButtonsActionsFragmentHolder;
    private FrameLayout          sitesActionsFragmentContainer;

    protected SitesButtons sitesButtons;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sliding_up_panel_detail );
        slider = ( SlidingUpPanelLayout ) findViewById( R.id.slider );
        slider.addView( initMainView( savedInstanceState ), 0 );
        vaButtonsActionsFragmentHolder = ( ViewAnimator ) findViewById( R.id.va_buttons_actions_fragment_holder );
        sitesButtons = initSitesButtons();
        vaButtonsActionsFragmentHolder.addView( sitesButtons, 0 );
        vaButtonsActionsFragmentHolder.setDisplayedChild( vaButtonsActionsFragmentHolder.indexOfChild( sitesButtons ) );
        sitesActionsFragmentContainer = ( FrameLayout ) findViewById( R.id.sites_actions_fragment_container );
        getSupportFragmentManager().beginTransaction()
                .add( sitesActionsFragmentContainer.getId(),
                        initSitesActionsFragment(),
                        getSitesActionsFragmentTag() )
                .commit();
        slider.setDragView( sitesButtons );
        slider.setEnableDragViewTouchEvents( true );
        slider.setPanelSlideListener( new SlidingUpPanelLayout.SimplePanelSlideListener()
        {
            @Override
            public void onPanelExpanded( View panel )
            {
                vaButtonsActionsFragmentHolder.setDisplayedChild( vaButtonsActionsFragmentHolder.indexOfChild( sitesActionsFragmentContainer ) );
            }

            @Override
            public void onPanelCollapsed( View panel )
            {
                vaButtonsActionsFragmentHolder.setDisplayedChild( vaButtonsActionsFragmentHolder.indexOfChild( sitesButtons ) );
            }
        } );
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putInt( ACTIVE_VIEW, vaButtonsActionsFragmentHolder.getDisplayedChild() );
    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState )
    {
        super.onRestoreInstanceState( savedInstanceState );
        vaButtonsActionsFragmentHolder.setDisplayedChild( savedInstanceState.getInt( ACTIVE_VIEW, 0 ) );
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        HashtaggerApp.bus.register( this );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        HashtaggerApp.bus.unregister( this );
    }

    @Override
    public void onBackPressed()
    {
        if ( slider.isPanelExpanded() )
        {
            slider.collapsePanel();
        }
        else
        {
            super.onBackPressed();
        }
    }

    protected abstract SitesButtons initSitesButtons();

    protected abstract View initMainView( Bundle savedInstanceState );

    protected abstract String getSitesActionsFragmentTag();

    protected abstract Fragment initSitesActionsFragment();
}
