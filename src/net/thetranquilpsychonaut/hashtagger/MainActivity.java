package net.thetranquilpsychonaut.hashtagger;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener
{
    /**
     * Called when the activity is first created.
     */
    ActionBar actionBar;
    ViewPager vpSitesPager;
    FragmentPagerAdapter vpSitesPagerAdapter;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        vpSitesPager = ( ViewPager )findViewById( R.id.vp_sites_pager );
        HashtaggerSitesAdapter vpSitesPagerAdapter = new HashtaggerSitesAdapter( getSupportFragmentManager( ), this );
        vpSitesPager.setAdapter( vpSitesPagerAdapter );
        vpSitesPager.setOnPageChangeListener( this );

        actionBar = getActionBar( );
        actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );

        for( int i = 0; i < HashtaggerApp.SITES.size( ); ++i )
        {
            actionBar.addTab( actionBar.newTab( ).setText( HashtaggerApp.SITES.get( i ) ).setTabListener( this ) );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater( ).inflate( R.menu.options_menu, menu );
        return true;
    }

    @Override
    public void onTabSelected( ActionBar.Tab tab, FragmentTransaction ft )
    {
        vpSitesPager.setCurrentItem( tab.getPosition( ) );
    }

    @Override
    public void onTabUnselected( ActionBar.Tab tab, FragmentTransaction ft )
    {

    }

    @Override
    public void onTabReselected( ActionBar.Tab tab, FragmentTransaction ft )
    {

    }

    @Override
    public void onPageScrolled( int i, float v, int i2 )
    {

    }

    @Override
    public void onPageSelected( int i )
    {
        actionBar.setSelectedNavigationItem( i );
    }

    @Override
    public void onPageScrollStateChanged( int i )
    {

    }
}
