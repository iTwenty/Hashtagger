package net.thetranquilpsychonaut.hashtagger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterListAdapter extends ArrayAdapter<Status>
{
    Context ctx;

    public TwitterListAdapter( Context context, int textViewResourceId, List<Status> objects )
    {
        super( context, textViewResourceId, objects );
        ctx = context;
    }

    class ViewHolder
    {
        TextView tvTwitter;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        View view = convertView;
        ViewHolder viewHolder;
        Status status = getItem( position );
        LayoutInflater inflater = ( LayoutInflater )ctx.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );

        if( view == null )
        {
            view = inflater.inflate( R.layout.fragment_twitter_list_row, null );
            viewHolder = new ViewHolder();
            viewHolder.tvTwitter = ( TextView )view.findViewById( R.id.tv_twitter );
            view.setTag( viewHolder );
        }
        else
        {
            viewHolder = ( ViewHolder )view.getTag();
        }
        viewHolder.tvTwitter.setText( status.getText() );
        return view;
    }
}
