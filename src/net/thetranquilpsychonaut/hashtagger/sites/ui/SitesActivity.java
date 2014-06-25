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
import net.thetranquilpsychonaut.hashtagger.sites.instagram.ui.InstagramFragment;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterFragment;
import net.thetranquilpsychonaut.hashtagger.utils.DefaultPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator.IconPagerIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SitesActivity extends NavDrawerActivity
{
    private ViewPager          sitesPager;
    private IconPagerIndicator sitesPagerIndicator;
    private SitesPagerAdapter  sitesPagerAdapter;
    private SearchView         svHashtag;
    private Stack<String>      hashtags;

    // We check and reset this flag in onHandleIntent() to know
    // if the current intent is because of a back press or not.
    // In case it is, we don't need to add the search query to
    // the stack.
    private boolean isSearchDueToBackPress = false;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        RelativeLayout rl = ( RelativeLayout ) getLayoutInflater().inflate(
                R.layout.activity_sites,
                null,
                false );

        dlNavDrawer.addView( rl, 0 );

        sitesPager = ( ViewPager ) findViewById( R.id.sites_pager );
        sitesPagerIndicator = ( IconPagerIndicator ) findViewById( R.id.sites_pager_indicator );
        sitesPagerAdapter = new SitesPagerAdapter( getSupportFragmentManager(), new ArrayList<PageDescriptor>() );
        sitesPager.setAdapter( sitesPagerAdapter );
        sitesPager.setOffscreenPageLimit( 2 );

        sitesPagerIndicator.setViewPager( sitesPager );
        hashtags = null == PersistentData.SitesActivityData.hashtags ?
                new Stack<String>() :
                PersistentData.SitesActivityData.hashtags;

        if ( !TextUtils.isEmpty( peekCurrentHashtag() ) )
        {
            setTitle( peekCurrentHashtag() );
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
        int twitterPosition = savedSitePositions[HashtaggerApp.TWITTER_VALUE];
        int gPlusPosition = savedSitePositions[HashtaggerApp.GPLUS_VALUE];
        int instagramPosition = savedSitePositions[HashtaggerApp.INSTAGRAM_VALUE];
        int facebookPosition = savedSitePositions[HashtaggerApp.FACEBOOK_VALUE];

        if ( twitterPosition == -1 )
        {
            sitesPagerAdapter.remove( TwitterFragment.DESCRIPTOR );
        }
        else
        {
            if ( sitesPagerAdapter.contains( TwitterFragment.DESCRIPTOR ) )
            {
                sitesPagerAdapter.move( TwitterFragment.DESCRIPTOR, twitterPosition );
            }
            else
            {
                sitesPagerAdapter.insert( TwitterFragment.DESCRIPTOR, twitterPosition );
            }
        }

        if ( gPlusPosition == -1 )
        {
            sitesPagerAdapter.remove( GPlusFragment.DESCRIPTOR );
        }
        else
        {
            if ( sitesPagerAdapter.contains( GPlusFragment.DESCRIPTOR ) )
            {
                sitesPagerAdapter.move( GPlusFragment.DESCRIPTOR, gPlusPosition );
            }
            else
            {
                sitesPagerAdapter.insert( GPlusFragment.DESCRIPTOR, gPlusPosition );
            }
        }

        if ( instagramPosition == -1 )
        {
            sitesPagerAdapter.remove( InstagramFragment.DESCRIPTOR );
        }
        else
        {
            if ( sitesPagerAdapter.contains( InstagramFragment.DESCRIPTOR ) )
            {
                sitesPagerAdapter.move( InstagramFragment.DESCRIPTOR, instagramPosition );
            }
            else
            {
                sitesPagerAdapter.insert( InstagramFragment.DESCRIPTOR, instagramPosition );
            }
        }

        if ( facebookPosition == -1 )
        {
            sitesPagerAdapter.remove( FacebookFragment.DESCRIPTOR );
        }
        else
        {
            if ( sitesPagerAdapter.contains( FacebookFragment.DESCRIPTOR ) )
            {
                sitesPagerAdapter.move( FacebookFragment.DESCRIPTOR, facebookPosition );
            }
            else
            {
                sitesPagerAdapter.insert( FacebookFragment.DESCRIPTOR, facebookPosition );
            }
        }

        sitesPagerIndicator.notifyIconSetChanged();
    }

    private int[] getSavedSitePositions()
    {
        int[] positions = new int[HashtaggerApp.TOTAL_SITES_COUNT];
        int activePosition = 0;
        positions[HashtaggerApp.TWITTER_VALUE] = DefaultPrefs.twitterActive ? activePosition++ : -1;
        positions[HashtaggerApp.GPLUS_VALUE] = DefaultPrefs.gPlusActive ? activePosition++ : -1;
        positions[HashtaggerApp.INSTAGRAM_VALUE] = DefaultPrefs.instagramActive ? activePosition++ : -1;
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
        saveHashtagItem.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick( MenuItem item )
            {
                doSaveHashtag( item );
                return true;
            }
        } );
        menu.findItem( R.id.it_settings ).setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick( MenuItem item )
            {
                doLaunchSettings( item );
                return true;
            }
        } );
        if ( !TextUtils.isEmpty( peekCurrentHashtag() ) )
        {
            saveHashtagItem.setTitle( "Save " + peekCurrentHashtag() );
            saveHashtagItem.setVisible( true );
        }
        else
        {
            saveHashtagItem.setVisible( false );
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        PersistentData.SitesActivityData.hashtags = this.hashtags;
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
        ContentValues values = new ContentValues();
        String hashtagToSave = peekCurrentHashtag();
        values.put( SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG, hashtagToSave );
        Uri result = getContentResolver()
                .insert( SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI, values );
        Toast.makeText(
                this,
                result == null ?
                        "Failed to save hashtag " + hashtagToSave :
                        "Saved hashtag " + hashtagToSave,
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
        // This method gets called before svHashtag is fully ready during a some
        // starts, causing NPE, so we need to check for null
        if ( null != svHashtag )
        {
            svHashtag.setIconified( true );
            svHashtag.onActionViewCollapsed();
        }
        final String input = intent.getStringExtra( SearchManager.QUERY );
        if ( TextUtils.isEmpty( input ) )
        {
            return;
        }

        // If the search is due to back press, we just popped the input
        // off the stack. We don't want to add it back in!
        if ( !isSearchDueToBackPress )
        {
            pushCurrentHashtag( input );
        }
        isSearchDueToBackPress = false;

        // No point continuing is network is not available
        if ( !HashtaggerApp.isNetworkConnected() )
        {
            Helper.showNoNetworkToast( this );
            return;
        }

        // We have a hashtag, time to show the save option in the menu
        invalidateOptionsMenu();
        // Save the entered query for later search suggestion
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                this,
                HashtagSuggestionsProvider.AUTHORITY,
                HashtagSuggestionsProvider.MODE );
        suggestions.saveRecentQuery( input, null );
        setTitle( input );
        new Handler( Looper.getMainLooper() ).post( new Runnable()
        {
            @Override
            public void run()
            {
                // Subscriber : SitesFragment : searchHashtag()
                HashtaggerApp.bus.post( new SearchHashtagEvent( input ) );
            }
        } );
    }

    @Override
    public void onBackPressed()
    {
        if ( Helper.isNullOrEmpty( hashtags ) || hashtags.size() == 1 )
        {
            super.onBackPressed();
            return;
        }
        popCurrentHashtag();
        String hashtag = peekCurrentHashtag();
        // This flag tells onHandleIntent() to not add the query back to the stack
        isSearchDueToBackPress = true;
        Intent intent = new Intent( Intent.ACTION_SEARCH );
        intent.putExtra( SearchManager.QUERY, hashtag );
        intent.setComponent( this.getComponentName() );
        startActivity( intent );
    }

    public String peekCurrentHashtag()
    {
        if ( Helper.isNullOrEmpty( hashtags ) )
        {
            return null;
        }
        return hashtags.peek();
    }

    public String popCurrentHashtag()
    {
        if ( Helper.isNullOrEmpty( hashtags ) )
        {
            return null;
        }
        return hashtags.pop();
    }

    public String pushCurrentHashtag( String hashtag )
    {
        return hashtags.push( hashtag );
    }
}
