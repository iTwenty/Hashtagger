package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import twitter4j.Status;

import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterListAdapter extends ArrayAdapter<Status>
{
    Context ctx;

    public TwitterListAdapter( Context context, int textViewResourceId, List<Status> statuses )
    {
        super( context, textViewResourceId, statuses );
        ctx = context;
    }

    class ViewHolder
    {
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        TwitterListRow twitterListRow = ( TwitterListRow ) convertView;
        Status status = getItem( position );
        if ( twitterListRow == null )
        {
            twitterListRow = new TwitterListRow( getContext() );
            twitterListRow.setTag( twitterListRow );
        }
        else
        {
            twitterListRow = ( TwitterListRow ) twitterListRow.getTag();
        }
        twitterListRow.showRow( status );
        Integer expandedPosition = ( Integer ) parent.getTag();
        if ( null != expandedPosition )
        {
            if ( expandedPosition == position )
            {
                twitterListRow.expandRow( status );
            }
            else
            {
                twitterListRow.collapseRow();
            }
        }
        return twitterListRow;
    }
}
