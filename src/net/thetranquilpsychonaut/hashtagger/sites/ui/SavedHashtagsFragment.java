package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.SavedHashtagDeletedEvent;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsDBContract;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsProviderContract;
import net.thetranquilpsychonaut.hashtagger.widgets.buttontoast.ButtonToast;
import net.thetranquilpsychonaut.hashtagger.widgets.buttontoast.Listeners;
import net.thetranquilpsychonaut.hashtagger.widgets.buttontoast.OnClickWrapper;

/**
 * Created by itwenty on 6/5/14.
 */
public class SavedHashtagsFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int SAVED_HASHTAG_LOADER = 0;

    private SavedHashtagsAdapter savedHashtagsAdapter;
    private ListView             lvSavedHashtags;
    private TextView             tvSavedHashtagsEmpty;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.fragment_saved_hashtags, container, false );
        lvSavedHashtags = ( ListView ) view.findViewById( R.id.lv_saved_hashtags );
        tvSavedHashtagsEmpty = ( TextView ) view.findViewById( R.id.tv_saved_hashtags_empty );
        lvSavedHashtags.setEmptyView( tvSavedHashtagsEmpty );
        savedHashtagsAdapter = new SavedHashtagsAdapter( container.getContext(), null );
        lvSavedHashtags.setAdapter( savedHashtagsAdapter );
        lvSavedHashtags.setOnItemClickListener( this );
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        super.onActivityCreated( savedInstanceState );
        getLoaderManager().initLoader( SAVED_HASHTAG_LOADER, null, this );
    }

    @Override
    public void onStart()
    {
        super.onStart();
        HashtaggerApp.bus.register( this );
    }

    @Override
    public void onStop()
    {
        super.onStop();
        HashtaggerApp.bus.unregister( this );
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        if ( parent.equals( lvSavedHashtags ) )
        {
            ( ( NavDrawerActivity ) getActivity() ).dlNavDrawer.closeDrawers();
            Cursor cursor = ( Cursor ) parent.getItemAtPosition( position );
            String selectedHashtag = cursor.getString( cursor.getColumnIndex( SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG ) );
            Intent intent = new Intent( Intent.ACTION_SEARCH );
            intent.putExtra( SearchManager.QUERY, selectedHashtag );
            intent.setComponent( new ComponentName( parent.getContext(), SitesActivity.class ) );
            parent.getContext().startActivity( intent );
        }
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
                return new CursorLoader( getActivity(), savedHashtagsUri, projection, null, null, null );
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
        ButtonToast.clearButtonToastsForActivity( getActivity() );
        ButtonToast toast = new ButtonToast( getActivity() );
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

    protected boolean restoreHashtag( String hashtag )
    {
        ContentValues values = new ContentValues();
        values.put( SavedHashtagsDBContract.SavedHashtags.COLUMN_HASHTAG, hashtag );
        Uri result = getActivity().getContentResolver().insert( SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI, values );
        return result == null ? false : true;
    }
}
