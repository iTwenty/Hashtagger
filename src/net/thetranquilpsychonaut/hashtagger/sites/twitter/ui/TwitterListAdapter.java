package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import twitter4j.Status;

import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterListAdapter extends SitesListAdapter
{

    public static final int STATUS_TYPE_NORMAL = 0;
    public static final int STATUS_TYPE_MEDIA  = 1;
    public static final int STATUS_TYPE_LINK   = 2;
    public static final int STATUS_TYPE_COUNT  = 3;

    protected TwitterListAdapter( Context context, int textViewResourceId, List<?> objects, List<Integer> resultTypes )
    {
        super( context, textViewResourceId, objects, resultTypes );
    }


    @Override
    public int getItemViewType( int position )
    {
        return resultTypes.get( position );
    }

    @Override
    public int getViewTypeCount()
    {
        return STATUS_TYPE_COUNT;
    }

    @Override
    protected SitesListRow getSitesListRow( Context context, int position, View convertView, ViewGroup parent )
    {
        switch ( getItemViewType( position ) )
        {
            case STATUS_TYPE_NORMAL:
                if ( null == convertView || !( convertView instanceof TwitterNormalRow ) )
                {
                    convertView = new TwitterNormalRow( context );
                }
                break;
            case STATUS_TYPE_MEDIA:
                if ( null == convertView || !( convertView instanceof TwitterMediaRow ) )
                {
                    convertView = new TwitterMediaRow( context );
                }
                break;
            case STATUS_TYPE_LINK:
                if ( null == convertView || !( convertView instanceof TwitterLinkRow ) )
                {
                    convertView = new TwitterLinkRow( context );
                }
                break;
        }
        return ( SitesListRow ) convertView;
    }

    public static int getStatusType( Status status )
    {
        int statusType = STATUS_TYPE_NORMAL;

        if ( status.getMediaEntities().length > 0 )
        {
            statusType = STATUS_TYPE_MEDIA;
        }
        else if ( status.getURLEntities().length > 0 )
        {
            statusType = STATUS_TYPE_LINK;
        }

        return statusType;
    }
}
