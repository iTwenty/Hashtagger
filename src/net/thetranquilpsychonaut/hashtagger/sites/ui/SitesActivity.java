package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import net.thetranquilpsychonaut.hashtagger.HashtagSuggestionsProvider;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.iconpagerindicator.IconPageIndicator;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsDBContract;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsDBHelper;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsProviderContract;

import java.util.List;

public class SitesActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener
{
    private static final int SAVED_HASHTAG_LOADER = 0;

    ActionBar             actionBar;
    DrawerLayout          dlNavDrawer;
    ActionBarDrawerToggle drawerToggle;
    ViewPager             vpSitesPager;
    IconPageIndicator     ipiSitesPager;
    ListView              lvSavedHashtags;
    TextView              tvSavedHashtagsEmpty;
    SitesAdapter          vpSitesPagerAdapter;
    SearchView            svHashtag;
    String                hashtag;
    SavedHashtagsAdapter  savedHashtagsAdapter;
    SavedHashtagsDBHelper savedHashtagsDBHelper;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sites );

        dlNavDrawer = ( DrawerLayout ) findViewById( R.id.dl_nav_drawer );
        drawerToggle = new ActionBarDrawerToggle(
                this,
                dlNavDrawer,
                R.drawable.ic_drawer,
                R.string.open_drawer_desc,
                R.string.close_drawer_desc );
        dlNavDrawer.setDrawerListener( drawerToggle );

        vpSitesPager = ( ViewPager ) findViewById( R.id.vp_sites_pager );
        ipiSitesPager = ( IconPageIndicator ) findViewById( R.id.ipi_sites_pager );
        SitesAdapter vpSitesPagerAdapter = new SitesAdapter( getSupportFragmentManager() );
        vpSitesPager.setAdapter( vpSitesPagerAdapter );
        vpSitesPager.setOffscreenPageLimit( HashtaggerApp.SITES.size() );
        ipiSitesPager.setViewPager( vpSitesPager );

        lvSavedHashtags = ( ListView ) findViewById( R.id.lv_saved_hashtags );
        tvSavedHashtagsEmpty = ( TextView ) findViewById( R.id.tv_saved_hashtags_empty );
        lvSavedHashtags.setEmptyView( tvSavedHashtagsEmpty );

        savedHashtagsDBHelper = new SavedHashtagsDBHelper( this );
        getSupportLoaderManager().initLoader( SAVED_HASHTAG_LOADER, null, this );

        savedHashtagsAdapter = new SavedHashtagsAdapter( this, null );
        lvSavedHashtags.setAdapter( savedHashtagsAdapter );
        lvSavedHashtags.setOnItemClickListener( this );

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeButtonEnabled( true );

        if ( null != getIntent() && getIntent().getAction().equals( Intent.ACTION_SEARCH ) )
        {
            handleIntent( getIntent() );
        }
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
    public boolean onOptionsItemSelected( MenuItem item )
    {
        if ( drawerToggle.onOptionsItemSelected( item ) )
        {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onPrepareOptionsMenu( Menu menu )
    {
        // Don't show save option if no hashtag has been entered
        menu.findItem( R.id.it_save_hashtag ).setVisible( null == this.hashtag ? false : true );
        return true;
    }

    public void doSaveHashtag( MenuItem item )
    {
        if ( null == this.hashtag || "".equals( this.hashtag ) )
        {
            return;
        }
        ContentValues values = new ContentValues();
        values.put( SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG, this.hashtag );
        Uri result = getContentResolver().insert( SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI, values );
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if ( null != fragments )
        {
            for ( Fragment f : fragments )
            {
                f.onActivityResult( requestCode, resultCode, data );
            }
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
        // Collapse the searchView, supposed to happen automatically, but doesn't :(
        svHashtag.setIconified( true );
        svHashtag.onActionViewCollapsed();
        // No point continuing is network is not available
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Toast.makeText( this, getResources().getString( R.string.str_toast_no_network ), Toast.LENGTH_LONG ).show();
            return;
        }
        String input = intent.getStringExtra( SearchManager.QUERY );
        hashtag = input.startsWith( "#" ) ? input : "#" + input;
        // We have a hashtag, time to show the save option in the menu
        invalidateOptionsMenu();
        // Save the entered query for later search suggestion
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
    public Loader<Cursor> onCreateLoader( int loaderId, Bundle bundle )
    {
        switch ( loaderId )
        {
            case SAVED_HASHTAG_LOADER:
                String[] projection = new String[]{
                        SavedHashtagsProviderContract.SavedHashtags._ID,
                        SavedHashtagsProviderContract.SavedHashtags.COLUMN_HASHTAG };
                Uri savedHashtagsUri = SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI;
                return new CursorLoader(
                        this,
                        savedHashtagsUri,
                        projection,
                        null,
                        null,
                        null );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished( Loader<Cursor> cursorLoader, Cursor cursor )
    {
        savedHashtagsAdapter.swapCursor( cursor );
    }

    @Override
    public void onLoaderReset( Loader<Cursor> cursorLoader )
    {
        savedHashtagsAdapter.swapCursor( null );
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        Cursor cursor = ( Cursor ) parent.getItemAtPosition( position );
        String selectedHashtag = cursor.getString( cursor.getColumnIndex( SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG ) );
        // Close the drawer first
        dlNavDrawer.closeDrawers();
        // We need to deliver the search intent manually in case a saved hashtag was selected
        Intent intent = new Intent( Intent.ACTION_SEARCH );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.putExtra( SearchManager.QUERY, selectedHashtag );
        intent.setComponent( getComponentName() );
        startActivity( intent );
    }
}
