package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/10/14.
 */
public abstract class NavDrawerActivity extends FragmentActivity
{
    protected DrawerLayout          dlNavDrawer;
    protected ActionBarDrawerToggle drawerToggle;
    protected ActionBar             actionBar;

    protected ViewPager               navDrawerPager;
    protected NavDrawerPagerIndicator navDrawerPagerIndicator;
    protected NavDrawerPagerAdapter   navDrawerPagerAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_nav_drawer );
        dlNavDrawer = ( DrawerLayout ) findViewById( R.id.dl_nav_drawer );
        drawerToggle = new ActionBarDrawerToggle(
                this,
                dlNavDrawer,
                R.drawable.ic_drawer,
                R.string.open_drawer_desc,
                R.string.close_drawer_desc );
        dlNavDrawer.setDrawerListener( drawerToggle );

        navDrawerPager = ( ViewPager ) findViewById( R.id.nav_drawer_pager );
        navDrawerPagerIndicator = ( NavDrawerPagerIndicator ) findViewById( R.id.nav_drawer_pager_indicator );

        navDrawerPagerAdapter = new NavDrawerPagerAdapter( getSupportFragmentManager() );
        navDrawerPager.setAdapter( navDrawerPagerAdapter );

        navDrawerPagerIndicator.setViewPager( navDrawerPager );

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeButtonEnabled( true );
    }

    @Override
    protected void onPostCreate( Bundle savedInstanceState )
    {
        super.onPostCreate( savedInstanceState );
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged( Configuration newConfig )
    {
        super.onConfigurationChanged( newConfig );
        drawerToggle.onConfigurationChanged( newConfig );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Needed for drawer toggle to work properly
        if ( drawerToggle.onOptionsItemSelected( item ) )
        {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private static class NavDrawerPagerAdapter extends FragmentPagerAdapter
    {
        public NavDrawerPagerAdapter( FragmentManager fm )
        {
            super( fm );
        }

        @Override
        public Fragment getItem( int position )
        {
            switch ( position )
            {
                case 0:
                    return new TrendingHashtagsFragment();
                case 1:
                    return new SavedHashtagsFragment();
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return 2;
        }
    }
}
