package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        View view = convertView;
        FacebookListRow facebookListRow;
        final Post post = getItem( position );
        if ( view == null )
        {
            view = LayoutInflater.from( ctx ).inflate( R.layout.fragment_facebook_list_row, null );
            facebookListRow = new FacebookListRow( view );
            view.setTag( facebookListRow );
        }
        else
        {
            facebookListRow = ( FacebookListRow ) view.getTag();
        }
        facebookListRow.showRow( post );
        Integer expandedPosition = ( Integer ) parent.getTag();
        if ( null != expandedPosition )
        {
            if ( expandedPosition == position )
            {
                facebookListRow.expandRow( post );
            }
            else
            {
                facebookListRow.collapseRow();
            }
        }
        return view;
    }
}
