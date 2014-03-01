package net.thetranquilpsychonaut.hashtagger;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener
{
    ActionBar                  actionBar;
    ViewPager                  vpSitesPager;
    HashtaggerSitesAdapter     vpSitesPagerAdapter;
    SearchView                 svHashtag;
    ConnectivityChangeReceiver connectivityChangeReceiver;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        vpSitesPager = ( ViewPager ) findViewById( R.id.vp_sites_pager );
        HashtaggerSitesAdapter vpSitesPagerAdapter = new HashtaggerSitesAdapter( getSupportFragmentManager(), this );
        vpSitesPager.setAdapter( vpSitesPagerAdapter );
        vpSitesPager.setOnPageChangeListener( this );
        vpSitesPager.setOffscreenPageLimit( HashtaggerApp.SITES.size() );

        actionBar = getActionBar();
        actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );

        for ( int i = 0; i < HashtaggerApp.SITES.size(); ++i )
        {
            actionBar.addTab( actionBar.newTab().setText( HashtaggerApp.SITES.get( i ) ).setTabListener( this ) );
        }

        if ( getIntent().getAction().equals( Intent.ACTION_SEARCH ) )
            handleIntent( getIntent() );
    }

    @Override
    protected void onResume()
    {
        this.connectivityChangeReceiver = new ConnectivityChangeReceiver();
        registerReceiver( connectivityChangeReceiver, new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION ) );
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        unregisterReceiver( connectivityChangeReceiver );
        super.onPause();
    }

    @Override
    protected void onNewIntent( Intent intent )
    {
        setIntent( intent );
        handleIntent( intent );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.options_menu, menu );
        SearchManager searchManager = ( SearchManager ) getSystemService( Context.SEARCH_SERVICE );
        svHashtag = ( SearchView ) menu.findItem( R.id.sv_hashtag ).getActionView();
        svHashtag.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );
        return true;
    }

    public void handleIntent( Intent intent )
    {
        String hashtag = intent.getStringExtra( SearchManager.QUERY );
        svHashtag.setIconified( true );
        svHashtag.onActionViewCollapsed();
        setTitle( hashtag );
        HashtaggerApp.bus.post( hashtag );
    }

    public ConnectivityChangeReceiver getConnectivityChangeReceiver()
    {
        return connectivityChangeReceiver;
    }

    @Override
    public void onTabSelected( ActionBar.Tab tab, FragmentTransaction ft )
    {
        vpSitesPager.setCurrentItem( tab.getPosition() );
    }

    @Override
    public void onPageSelected( int i )
    {
        actionBar.setSelectedNavigationItem( i );
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
    public void onPageScrollStateChanged( int i )
    {
    }
}
