package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.SavedHashtagDeletedEvent;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsDBContract;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsDBHelper;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsProviderContract;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.buttontoast.ButtonToast;
import net.thetranquilpsychonaut.hashtagger.widgets.buttontoast.Listeners;
import net.thetranquilpsychonaut.hashtagger.widgets.buttontoast.OnClickWrapper;

/**
 * Created by itwenty on 5/10/14.
 */
public class SavedHashtagsActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener
{
    private static final int SAVED_HASHTAG_LOADER = 0;

    protected DrawerLayout          dlNavDrawer;
    protected ActionBarDrawerToggle drawerToggle;
    protected ActionBar             actionBar;
    protected ListView              lvSavedHashtags;
    protected TextView              tvSavedHashtagsEmpty;
    protected SavedHashtagsAdapter  savedHashtagsAdapter;
    protected SavedHashtagsDBHelper savedHashtagsDBHelper;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_saved_hashtags );
        dlNavDrawer = ( DrawerLayout ) findViewById( R.id.dl_nav_drawer );
        drawerToggle = new ActionBarDrawerToggle(
                this,
                dlNavDrawer,
                R.drawable.ic_drawer,
                R.string.open_drawer_desc,
                R.string.close_drawer_desc );
        dlNavDrawer.setDrawerListener( drawerToggle );

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
    }

    @Override
    protected void onPostCreate( Bundle savedInstanceState )
    {
        super.onPostCreate( savedInstanceState );
        drawerToggle.syncState();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        HashtaggerApp.bus.register( this );
    }

    @Override
    protected void onPause()
    {
        HashtaggerApp.bus.unregister( this );
        super.onPause();
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

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        Cursor cursor = ( Cursor ) parent.getItemAtPosition( position );
        String selectedHashtag = cursor.getString( cursor.getColumnIndex( SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG ) );
        // Close the drawer first
        dlNavDrawer.closeDrawers();
        Helper.debug( selectedHashtag );
        // We need to deliver the search intent manually in case a saved hashtag was selected
//        Intent intent = new Intent( Intent.ACTION_SEARCH );
//        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
//        intent.putExtra( SearchManager.QUERY, selectedHashtag );
//        intent.setComponent( getComponentName() );
//        startActivity( intent );
    }

    @Subscribe
    public void onSavedHashtagDeleted( final SavedHashtagDeletedEvent event )
    {
        dlNavDrawer.closeDrawers();
        ButtonToast toast = new ButtonToast( this );
        toast.setIndeterminate( true );
        toast.setTouchToDismiss( true );
        toast.setText( "Hashtag " + event.getDeletedHashtag() + " deleted" );
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

    protected boolean restoreHashtag( String hashtag )
    {
        ContentValues values = new ContentValues();
        values.put( SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG, hashtag );
        Uri result = getContentResolver().insert( SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI, values );
        return result == null ? false : true;
    }
}
