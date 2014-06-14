package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;
import net.thetranquilpsychonaut.hashtagger.HashtagSuggestionsProvider;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.PageDescriptor;
import net.thetranquilpsychonaut.hashtagger.events.SearchHashtagEvent;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsDBContract;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsProviderContract;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.ui.FacebookFragment;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.ui.GPlusFragment;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterFragment;
import net.thetranquilpsychonaut.hashtagger.utils.DefaultPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class SitesActivity extends NavDrawerActivity
{
    public static String currentHashtag = null;

    ViewPager           vpSitesPager;
    SitesPagerIndicator ipiSitesPager;
    SitesAdapter        vpSitesPagerAdapter;
    SearchView          svHashtag;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        RelativeLayout rl = ( RelativeLayout ) getLayoutInflater().inflate(
                R.layout.activity_sites,
                null,
                false );

        dlNavDrawer.addView( rl, 0 );

        vpSitesPager = ( ViewPager ) findViewById( R.id.vp_sites_pager );
        ipiSitesPager = ( SitesPagerIndicator ) findViewById( R.id.ipi_sites_pager );
        vpSitesPagerAdapter = new SitesAdapter( getSupportFragmentManager(), new ArrayList<PageDescriptor>() );
        vpSitesPager.setAdapter( vpSitesPagerAdapter );
        vpSitesPager.setOffscreenPageLimit( 2 );

        ipiSitesPager.setViewPager( vpSitesPager );

        if ( !TextUtils.isEmpty( currentHashtag ) )
        {
            setTitle( currentHashtag );
        }

        showActiveSites();
    }

    @Override
    protected void onResumeFragments()
    {
        super.onResumeFragments();
        if ( DefaultPrefs.activeSitesChanged )
        {
            showActiveSites();
            DefaultPrefs.activeSitesChanged = false;
        }
    }

    public void showActiveSites()
    {
        int[] savedSitePositions = getSavedSitePositions();
        ipiSitesPager.removeAllViews();
        int twitterPosition = savedSitePositions[HashtaggerApp.TWITTER_VALUE];
        int gPlusPosition = savedSitePositions[HashtaggerApp.GPLUS_VALUE];
        int facebookPosition = savedSitePositions[HashtaggerApp.FACEBOOK_VALUE];
        if ( twitterPosition == -1 )
        {
            vpSitesPagerAdapter.remove( TwitterFragment.descriptor );
        }
        else
        {
            ipiSitesPager.addIcon( HashtaggerApp.TWITTER_VALUE );
            if ( vpSitesPagerAdapter.contains( TwitterFragment.descriptor ) )
            {
                vpSitesPagerAdapter.move( TwitterFragment.descriptor, twitterPosition );
            }
            else
            {
                vpSitesPagerAdapter.insert( TwitterFragment.descriptor, twitterPosition );
            }
        }
        if ( gPlusPosition == -1 )
        {
            vpSitesPagerAdapter.remove( GPlusFragment.descriptor );
        }
        else
        {
            ipiSitesPager.addIcon( HashtaggerApp.GPLUS_VALUE );
            if ( vpSitesPagerAdapter.contains( GPlusFragment.descriptor ) )
            {
                vpSitesPagerAdapter.move( GPlusFragment.descriptor, gPlusPosition );
            }
            else
            {
                vpSitesPagerAdapter.insert( GPlusFragment.descriptor, gPlusPosition );
            }
        }
        if ( facebookPosition == -1 )
        {
            vpSitesPagerAdapter.remove( FacebookFragment.descriptor );
        }
        else
        {
            ipiSitesPager.addIcon( HashtaggerApp.FACEBOOK_VALUE );
            if ( vpSitesPagerAdapter.contains( FacebookFragment.descriptor ) )
            {
                vpSitesPagerAdapter.move( FacebookFragment.descriptor, facebookPosition );
            }
            else
            {
                vpSitesPagerAdapter.insert( FacebookFragment.descriptor, facebookPosition );
            }
        }
        ipiSitesPager.setSelectedChild( vpSitesPager.getCurrentItem() );
    }

    private int[] getSavedSitePositions()
    {
        int[] positions = new int[HashtaggerApp.TOTAL_SITES_COUNT];
        int activePosition = 0;
        positions[HashtaggerApp.TWITTER_VALUE] = DefaultPrefs.twitterActive ? activePosition++ : -1;
        positions[HashtaggerApp.GPLUS_VALUE] = DefaultPrefs.gPlusActive ? activePosition++ : -1;
        positions[HashtaggerApp.FACEBOOK_VALUE] = DefaultPrefs.facebookActive ? activePosition++ : -1;
        return positions;
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
        getMenuInflater().inflate( R.menu.activity_sites_menu, menu );
        SearchManager searchManager = ( SearchManager ) getSystemService( Context.SEARCH_SERVICE );
        svHashtag = ( SearchView ) menu.findItem( R.id.sv_hashtag ).getActionView();
        svHashtag.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );
        MenuItem saveHashtagItem = menu.findItem( R.id.it_save_hashtag );
        if ( TextUtils.isEmpty( currentHashtag ) )
        {
            saveHashtagItem.setVisible( false );
        }
        else
        {
            saveHashtagItem.setTitle( "Save " + currentHashtag );
            saveHashtagItem.setVisible( true );
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // We handle logout in SitesFragment, so return false here
        if ( item.getItemId() == R.id.it_logout )
        {
            return false;
        }
        return super.onOptionsItemSelected( item );
    }

    public void doSaveHashtag( MenuItem item )
    {
        if ( TextUtils.isEmpty( currentHashtag ) )
        {
            return;
        }
        ContentValues values = new ContentValues();
        values.put( SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG, currentHashtag );
        Uri result = getContentResolver()
                .insert( SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI, values );
        Toast.makeText(
                this,
                result == null ?
                        "Failed to save hashtag " + currentHashtag :
                        "Saved hashtag " + currentHashtag,
                Toast.LENGTH_SHORT )
                .show();
    }

    public void doLaunchSettings( MenuItem item )
    {
        startActivity( new Intent( this, SettingsActivity.class ) );
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

    public void handleIntent( Intent intent )
    {
        // Collapse the searchView, supposed to happen automatically, but doesn't :(
        svHashtag.setIconified( true );
        svHashtag.onActionViewCollapsed();
        String input = intent.getStringExtra( SearchManager.QUERY );
        if ( TextUtils.isEmpty( input ) )
        {
            return;
        }
        // No point continuing is network is not available
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( this );
            return;
        }

        currentHashtag = input;
        // We have a hashtag, time to show the save option in the menu
        invalidateOptionsMenu();
        // Save the entered query for later search suggestion
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                this,
                HashtagSuggestionsProvider.AUTHORITY,
                HashtagSuggestionsProvider.MODE );
        suggestions.saveRecentQuery( currentHashtag, null );
        setTitle( currentHashtag );
        new Handler( Looper.getMainLooper() ).post( new Runnable()
        {
            @Override
            public void run()
            {
                // Subscriber : SitesFragment : searchHashtag()
                HashtaggerApp.bus.post( new SearchHashtagEvent() );
            }
        } );
    }

    public static final String getCurrentHashtag()
    {
        return currentHashtag;
    }
}
