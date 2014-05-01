package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterListAdapter extends SitesListAdapter
{
    public static final  int STATUS_TYPE_NORMAL = 0;
    public static final  int STATUS_TYPE_MEDIA  = 1;
    public static final  int STATUS_TYPE_LINK   = 2;
    private static final int STATUS_TYPE_COUNT  = 3;
    private List<Integer> statusTypes;

    public TwitterListAdapter( Context context, int textViewResourceId, List<?> results )
    {
        super( context, textViewResourceId, results );
        statusTypes = new ArrayList<Integer>();
    }

    @Override
    public int getItemViewType( int position )
    {
        return statusTypes.get( position );
    }

    @Override
    public int getViewTypeCount()
    {
        return STATUS_TYPE_COUNT;
    }

    @Override
    protected SitesListRow initSitesListRow( Context context, int position, View convertView, ViewGroup parent )
    {
        switch ( getItemViewType( position ) )
        {
            case STATUS_TYPE_NORMAL:
                if ( null == convertView || !( convertView instanceof TwitterNormalRow )  )
                    convertView = new TwitterNormalRow( context );
                break;
            case STATUS_TYPE_MEDIA:
                if ( null == convertView || !( convertView instanceof TwitterMediaRow )  )
                    convertView = new TwitterMediaRow( context );
                break;
            case STATUS_TYPE_LINK:
                if ( null == convertView || !( convertView instanceof TwitterLinkRow )  )
                    convertView = new TwitterLinkRow( context );
                break;
        }
        return ( SitesListRow ) convertView;
    }

    @Override
    public void updateTypes( SearchType searchType, List<?> searchResults )
    {
        List<Integer> newTypes = new ArrayList<Integer>( searchResults.size() );
        for ( Status status : ( List<Status> ) searchResults )
        {
            if ( status.getMediaEntities().length > 0 )
                newTypes.add( STATUS_TYPE_MEDIA );
            else if ( status.getURLEntities().length > 0 )
                newTypes.add( STATUS_TYPE_LINK );
            else
                newTypes.add( STATUS_TYPE_NORMAL );
        }
        if ( searchType == SearchType.NEWER )
            statusTypes.addAll( 0, newTypes );
        else
            statusTypes.addAll( newTypes );
    }
}
