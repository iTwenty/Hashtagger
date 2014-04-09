package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;

import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookListAdapter extends ArrayAdapter<Post>
{
    Context ctx;

    public FacebookListAdapter( Context context, int textViewResourceId, List<Post> posts )
    {
        super( context, textViewResourceId, posts );
        this.ctx = context;
    }

    class ViewHolder
    {
        TextView tvFacebookRow;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        View view = convertView;
        ViewHolder viewHolder;
        Post post = getItem( position );
        LayoutInflater inflater = ( LayoutInflater ) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        if( view == null )
        {
            view = inflater.inflate( R.layout.fragment_facebook_list_row, null );
            viewHolder = new ViewHolder();
            viewHolder.tvFacebookRow = ( TextView ) view.findViewById( R.id.tv_facebook_row );
            view.setTag( viewHolder );
        }
        else
        {
            viewHolder = ( ViewHolder ) view.getTag();
        }
        viewHolder.tvFacebookRow.setText( post.getMessage() );
        return view;
    }
}
