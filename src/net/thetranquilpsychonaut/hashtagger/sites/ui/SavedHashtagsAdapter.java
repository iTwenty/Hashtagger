package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;

import java.util.List;

/**
 * Created by itwenty on 4/22/14.
 */
public class SavedHashtagsAdapter extends ArrayAdapter<String>
{
    Context context;

    public SavedHashtagsAdapter( Context context, int textViewResourceId, List<String> objects )
    {
        super( context, textViewResourceId, objects );
        this.context = context;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        String hashtag = getItem( position );
        ViewHolder holder;
        if ( convertView == null )
        {
            convertView = LayoutInflater.from( context ).inflate( R.layout.saved_hashtags_list_row, null );
            holder = new ViewHolder();
            holder.tvSavedHashtag = ( TextView ) convertView.findViewById( R.id.tv_saved_hashtag );
            holder.imgbSavedHashtagDelete = ( ImageButton ) convertView.findViewById( R.id.imgb_delete_saved_hashtag );
            convertView.setTag( holder );
        }
        else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        holder.tvSavedHashtag.setText( hashtag );
        return convertView;
    }

    private static class ViewHolder
    {
        TextView    tvSavedHashtag;
        ImageButton imgbSavedHashtagDelete;
    }
}
