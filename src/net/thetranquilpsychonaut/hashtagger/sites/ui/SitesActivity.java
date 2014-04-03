package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;

import java.util.List;

public class SitesActivity extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener
{
    private static final String TITLE_KEY = HashtaggerApp.NAMESPACE + "title_key";
    ActionBar    actionBar;
    ViewPager    vpSitesPager;
    SitesAdapter vpSitesPagerAdapter;
    SearchView   svHashtag;
    String       hashtag;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sites );
        vpSitesPager = ( ViewPager ) findViewById( R.id.vp_sites_pager );
        SitesAdapter vpSitesPagerAdapter = new SitesAdapter( getSupportFragmentManager() );
        vpSitesPager.setAdapter( vpSitesPagerAdapter );
        vpSitesPager.setOnPageChangeListener( this );
        vpSitesPager.setOffscreenPageLimit( HashtaggerApp.SITES.size() );

        actionBar = getActionBar();
        actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );

        for ( int i = 0; i < HashtaggerApp.SITES.size(); ++i )
        {
            actionBar.addTab( actionBar.newTab().setText( HashtaggerApp.SITES.get( i ) ).setTabListener( this ) );
        }

        if ( null != getIntent() && getIntent().getAction().equals( Intent.ACTION_SEARCH ) )
            handleIntent( getIntent() );
    }

    @Override
    protected void onNewIntent( Intent intent )
    {
        setIntent( intent );
        handleIntent( intent );
        setIntent( null );
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

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        Helper.debug( "activity onactivityresult" );
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if ( null != fragments )
        {
            for ( Fragment f : fragments )
                f.onActivityResult( requestCode, resultCode, data );
        }
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putString( HashtaggerApp.HASHTAG_KEY, hashtag );
        outState.putCharSequence( TITLE_KEY, getTitle() );
    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState )
    {
        this.hashtag = savedInstanceState.getString( HashtaggerApp.HASHTAG_KEY );
        setTitle( savedInstanceState.getCharSequence( TITLE_KEY ) );
    }

    public void handleIntent( Intent intent )
    {
        Helper.debug( "onHandleIntent" );
        svHashtag.setIconified( true );
        svHashtag.onActionViewCollapsed();
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( this, getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        String input = intent.getStringExtra( SearchManager.QUERY );
        hashtag = input.startsWith( "#" ) ? input : "#" + input;
        setTitle( hashtag );
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if ( null != fragments )
        {
            for ( Fragment f : fragments )
            {
                if ( f instanceof SitesFragment )
                {
                    ( ( SitesFragment ) f ).searchHashtag( hashtag );
                }
            }
        }
    }


    public String getHashtag()
    {
        return hashtag;
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
