package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import net.thetranquilpsychonaut.hashtagger.HashtagSuggestionsProvider;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SavedHashtagsHandler;

import java.io.IOException;
import java.util.List;

public class SitesActivity extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener
{
    ActionBar            actionBar;
    ViewPager            vpSitesPager;
    ListView             lvSavedHashtags;
    TextView             tvSavedHashtagsEmpty;
    SitesAdapter         vpSitesPagerAdapter;
    SearchView           svHashtag;
    String               hashtag;
    SavedHashtagsHandler savedHashtagsHandler;
    SavedHashtagsAdapter savedHashtagsAdapter;

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

        lvSavedHashtags = ( ListView ) findViewById( R.id.lv_saved_hashtags );
        tvSavedHashtagsEmpty = ( TextView ) findViewById( R.id.tv_saved_hashtags_empty );
        lvSavedHashtags.setEmptyView( tvSavedHashtagsEmpty );
        try
        {
            savedHashtagsHandler = new SavedHashtagsHandler();
            savedHashtagsAdapter = new SavedHashtagsAdapter( this, R.layout.saved_hashtags_list_row, savedHashtagsHandler.getSavedHashtags() );
            lvSavedHashtags.setAdapter( savedHashtagsAdapter );
        }
        catch ( IOException e )
        {
            tvSavedHashtagsEmpty.setText( "Could not load saved hashtags." );
        }

        actionBar = getActionBar();
        actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );
        actionBar.addTab( actionBar.newTab().setText( HashtaggerApp.SITES.get( 0 ) ).setIcon( getResources().getDrawable( R.drawable.twitter_logo_monochrome ) ).setTabListener( this ) );
        actionBar.addTab( actionBar.newTab().setText( HashtaggerApp.SITES.get( 1 ) ).setIcon( getResources().getDrawable( R.drawable.facebook_logo_monochrome ) ).setTabListener( this ) );

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
    public boolean onPrepareOptionsMenu( Menu menu )
    {
        menu.findItem( R.id.it_save_hashtag ).setVisible( null == this.hashtag ? false : true );
        return true;
    }

    public void doSaveHashtag( MenuItem item )
    {
        if ( null == savedHashtagsHandler )
        {
            Toast.makeText( this, "Failed to save hashtag " + this.hashtag, Toast.LENGTH_LONG );
            return;
        }
        if ( null == this.hashtag || "".equals( this.hashtag ) )
            return;
        try
        {
            savedHashtagsHandler.saveHashtag( this.hashtag );
            Toast.makeText( this, "Hashtag " + this.hashtag + " saved.", Toast.LENGTH_LONG );
        }
        catch ( IOException e )
        {
            Toast.makeText( this, "Failed to save hashtag " + this.hashtag, Toast.LENGTH_LONG );
            return;
        }
        savedHashtagsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
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
    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState )
    {
        super.onRestoreInstanceState( savedInstanceState );
        this.hashtag = savedInstanceState.getString( HashtaggerApp.HASHTAG_KEY );
        setTitle( null == this.hashtag ? getResources().getString( R.string.app_name ) : this.hashtag );
    }

    public void handleIntent( Intent intent )
    {
        svHashtag.setIconified( true );
        svHashtag.onActionViewCollapsed();
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( this, getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        String input = intent.getStringExtra( SearchManager.QUERY );
        hashtag = input.startsWith( "#" ) ? input : "#" + input;
        invalidateOptionsMenu();
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions( this, HashtagSuggestionsProvider.AUTHORITY, HashtagSuggestionsProvider.MODE );
        suggestions.saveRecentQuery( hashtag, null );
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
