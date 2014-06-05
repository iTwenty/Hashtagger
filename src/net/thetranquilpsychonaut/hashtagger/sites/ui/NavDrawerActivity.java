package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.ActionBar;
import android.content.*;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.SavedHashtagDeletedEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterTrendsEvent;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsDBContract;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsProviderContract;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterTrendsService;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.buttontoast.ButtonToast;
import net.thetranquilpsychonaut.hashtagger.widgets.buttontoast.Listeners;
import net.thetranquilpsychonaut.hashtagger.widgets.buttontoast.OnClickWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 5/10/14.
 */
public abstract class NavDrawerActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener
{
    private static final int SAVED_HASHTAG_LOADER = 0;

    protected DrawerLayout          dlNavDrawer;
    protected ActionBarDrawerToggle drawerToggle;
    protected ActionBar             actionBar;

    protected ViewPager               navDrawerPager;
    protected NavDrawerPagerIndicator navDrawerPagerIndicator;
    protected NavDrawerPagerAdapter   navDrawerPagerAdapter;

    protected SavedHashtagsAdapter savedHashtagsAdapter;
    protected ListView             lvSavedHashtags;
    protected TextView             tvSavedHashtagsEmpty;

    protected TrendingHashtagsAdapter trendingHashtagsAdapter;
    protected List<String>            trendingHashtags;
    protected ListView                lvTrendingHashtags;
    protected TextView                tvTrendingHashtagsEmpty;
    protected TextView                tvTrendingHeading;
    protected TwitterTrendsService    twitterTrendsService;
    protected boolean                 isBoundToTrendsService;
    protected TwitterTrendsConnection twitterTrendsConnection;

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

        navDrawerPagerAdapter = new NavDrawerPagerAdapter();
        navDrawerPager.setAdapter( navDrawerPagerAdapter );

        navDrawerPagerIndicator.setViewPager( navDrawerPager );

        savedHashtagsAdapter = new SavedHashtagsAdapter( this, null );

        trendingHashtags = null == savedInstanceState ?
                new ArrayList<String>() :
                ( List<String> ) getLastCustomNonConfigurationInstance();
        trendingHashtagsAdapter = new TrendingHashtagsAdapter( this, trendingHashtags );
        isBoundToTrendsService = false;
        twitterTrendsConnection = new TwitterTrendsConnection();

        getSupportLoaderManager().initLoader( SAVED_HASHTAG_LOADER, null, this );

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeButtonEnabled( true );
    }

    @Override
    protected void onPostCreate( Bundle savedInstanceState )
    {
        super.onPostCreate( savedInstanceState );
        drawerToggle.syncState();
        if ( null == savedInstanceState )
        {
            navDrawerPager.post( new Runnable()
            {
                @Override
                public void run()
                {
                    if ( null != twitterTrendsService )
                        twitterTrendsService.fetchTrends();
                }
            } );
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        HashtaggerApp.bus.register( this );
        Intent i = new Intent( this, TwitterTrendsService.class );
        bindService( i, twitterTrendsConnection, Context.BIND_AUTO_CREATE );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        HashtaggerApp.bus.unregister( this );
        if ( isBoundToTrendsService )
        {
            unbindService( twitterTrendsConnection );
            isBoundToTrendsService = false;
        }
    }

    @Override
    public void onConfigurationChanged( Configuration newConfig )
    {
        super.onConfigurationChanged( newConfig );
        drawerToggle.onConfigurationChanged( newConfig );
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance()
    {
        return trendingHashtags;
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

    @Override
    public Loader<Cursor> onCreateLoader( int loaderId, Bundle bundle )
    {
        switch ( loaderId )
        {
            case SAVED_HASHTAG_LOADER:
                String[] projection = new String[]{ SavedHashtagsProviderContract.SavedHashtags._ID,
                        SavedHashtagsProviderContract.SavedHashtags.COLUMN_HASHTAG };
                Uri savedHashtagsUri = SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI;
                return new CursorLoader( this, savedHashtagsUri, projection, null, null, null );
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

    @Subscribe
    public void onSavedHashtagDeleted( final SavedHashtagDeletedEvent event )
    {
        dlNavDrawer.closeDrawers();
        ButtonToast.clearButtonToastsForActivity( this );
        ButtonToast toast = new ButtonToast( this );
        toast.setIndeterminate( true );
        toast.setTouchToDismiss( true );
        toast.setText( event.getDeletedHashtag() + " deleted" );
        toast.setOnClickWrapper( new OnClickWrapper( "DELETED_HASHTAG", new Listeners.OnClickListener()
        {
            @Override
            public void onClick( View view, Parcelable token )
            {
                restoreHashtag( event.getDeletedHashtag() );
            }
        } ) );
        toast.show();
    }

    @Subscribe
    public void onTwitterTrendsFound( TwitterTrendsEvent event )
    {
        int code = event.getStatus();
        if ( code == TwitterTrendsService.TRENDS_FOUND )
        {
            List<String> trends = event.getTrends();
            trendingHashtags.clear();
            trendingHashtags.addAll( trends );
            tvTrendingHeading.setText( String.format( "Trending in %s", event.getPlace() ) );
            trendingHashtagsAdapter.notifyDataSetChanged();
        }
        else if ( code == TwitterTrendsService.TRENDS_NOT_AVAILABLE )
        {
            tvTrendingHashtagsEmpty.setText( "Trends not available" );
        }
        else if ( code == TwitterTrendsService.TWITTER_NOT_LOGGED_IN )
        {
            tvTrendingHashtagsEmpty.setText( "Log in to Twitter to see trending topics" );
        }
    }

    protected boolean restoreHashtag( String hashtag )
    {
        ContentValues values = new ContentValues();
        values.put( SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG, hashtag );
        Uri result = getContentResolver().insert( SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI, values );
        return result == null ? false : true;
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        if ( parent.equals( lvSavedHashtags ) )
        {
            onSavedHashtagClick( parent, view, position, id );
            return;
        }
        if ( parent.equals( lvTrendingHashtags ) )
        {
            onTrendingHashtagClick( parent, view, position, id );
        }
    }

    protected abstract void onTrendingHashtagClick( AdapterView<?> parent, View view, int position, long id );

    protected abstract void onSavedHashtagClick( AdapterView<?> parent, View view, int position, long id );

    private class NavDrawerPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public boolean isViewFromObject( View view, Object o )
        {
            return view == o;
        }

        @Override
        public Object instantiateItem( ViewGroup container, int position )
        {
            View view = null;
            switch ( position )
            {
                case 0:
                    view = LayoutInflater.from( container.getContext() ).inflate( R.layout.nav_drawer_trending, container, false );
                    lvTrendingHashtags = ( ListView ) view.findViewById( R.id.lv_trending );
                    tvTrendingHashtagsEmpty = ( TextView ) view.findViewById( R.id.tv_trending_empty );
                    tvTrendingHeading = ( TextView ) view.findViewById( R.id.tv_trending_heading );
                    lvTrendingHashtags.setEmptyView( tvTrendingHashtagsEmpty );
                    lvTrendingHashtags.setAdapter( trendingHashtagsAdapter );
                    lvTrendingHashtags.setOnItemClickListener( NavDrawerActivity.this );
                    break;
                case 1:
                    view = LayoutInflater.from( container.getContext() ).inflate( R.layout.nav_drawer_saved, container, false );
                    lvSavedHashtags = ( ListView ) view.findViewById( R.id.lv_saved_hashtags );
                    tvSavedHashtagsEmpty = ( TextView ) view.findViewById( R.id.tv_saved_hashtags_empty );
                    lvSavedHashtags.setEmptyView( tvSavedHashtagsEmpty );
                    lvSavedHashtags.setAdapter( savedHashtagsAdapter );
                    lvSavedHashtags.setOnItemClickListener( NavDrawerActivity.this );
                    break;
            }
            container.addView( view, 0 );
            return view;
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView( ( View ) object );
        }
    }

    private class TwitterTrendsConnection implements ServiceConnection
    {
        @Override
        public void onServiceConnected( ComponentName name, IBinder service )
        {
            Helper.debug( "onServiceConnected" );
            TwitterTrendsService.TwitterTrendsBinder binder = ( TwitterTrendsService.TwitterTrendsBinder ) service;
            twitterTrendsService = binder.getService();
            isBoundToTrendsService = true;
        }

        @Override
        public void onServiceDisconnected( ComponentName name )
        {
            isBoundToTrendsService = false;
        }
    }
}
