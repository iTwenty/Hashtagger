package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.savedhashtags.SavedHashtagsProviderContract;

/**
 * Created by itwenty on 4/24/14.
 */
public class SavedHashtagsAdapter extends CursorAdapter implements View.OnClickListener
{
    Context context;

    public SavedHashtagsAdapter( Context context, Cursor cursor )
    {
        super( context, cursor, 0 );
        this.context = context;
    }

    private static class ViewHolder
    {
        TextView    tvSavedHashtag;
        ImageButton imgbDeleteSavedHashtag;
    }

    @Override
    public View newView( final Context context, Cursor cursor, ViewGroup parent )
    {
        View view = LayoutInflater.from( context ).inflate( R.layout.saved_hashtags_list_row, null );
        ViewHolder holder = new ViewHolder();
        holder.tvSavedHashtag = ( TextView ) view.findViewById( R.id.tv_saved_hashtag );
        holder.imgbDeleteSavedHashtag = ( ImageButton ) view.findViewById( R.id.imgb_delete_saved_hashtag );
        holder.imgbDeleteSavedHashtag.setFocusable( false );
        holder.imgbDeleteSavedHashtag.setOnClickListener( this );
        view.setTag( holder );
        return view;
    }

    @Override
    public void bindView( View view, Context context, Cursor cursor )
    {
        ViewHolder holder = ( ViewHolder ) view.getTag();
        holder.tvSavedHashtag.setText( cursor.getString( cursor.getColumnIndex( SavedHashtagsProviderContract.SavedHashtags.COLUMN_HASHTAG ) ) );
        holder.imgbDeleteSavedHashtag.setTag( cursor.getInt( cursor.getColumnIndex( SavedHashtagsProviderContract.SavedHashtags._ID ) ) );
    }

    @Override
    public void onClick( View v )
    {
        context.getContentResolver().delete( Uri.parse( SavedHashtagsProviderContract.SavedHashtags.CONTENT_URI + "/" + v.getTag() ), null, null );
    }
}