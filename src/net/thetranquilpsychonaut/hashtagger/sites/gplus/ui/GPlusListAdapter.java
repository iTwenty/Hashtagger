package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusListAdapter extends SitesListAdapter
{
    public static final int ACTIVITY_TYPE_NORMAL = 0;
    public static final int ACTIVITY_TYPE_MEDIA  = 1;
    public static final int ACTIVITY_TYPE_LINK   = 2;
    public static final int ACTIVITY_TYPE_COUNT  = 3;

    protected GPlusListAdapter( Context context, int textViewResourceId, List<?> objects, List<Integer> resultTypes )
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
        return ACTIVITY_TYPE_COUNT;
    }

    @Override
    protected SitesListRow getSitesListRow( Context context, int position, View convertView, ViewGroup parent )
    {
        switch ( getItemViewType( position ) )
        {
            case ACTIVITY_TYPE_NORMAL:
                if ( null == convertView || !( convertView instanceof GPlusNormalRow ) )
                {
                    convertView = new GPlusNormalRow( context );
                }
                break;
            case ACTIVITY_TYPE_MEDIA:
                if ( null == convertView || !( convertView instanceof GPlusMediaRow ) )
                {
                    convertView = new GPlusMediaRow( context );
                }
                break;
            case ACTIVITY_TYPE_LINK:
                if ( null == convertView || !( convertView instanceof GPlusLinkRow ) )
                {
                    convertView = new GPlusLinkRow( context );
                }
        }
        return ( SitesListRow ) convertView;
    }

    @Override
    public void updateTypes( SearchType searchType, List<?> searchResults )
    {
        if ( searchResults.isEmpty() )
        {
            return;
        }

        List<Integer> newTypes = new ArrayList<Integer>( searchResults.size() );
        for ( Activity activity : ( List<Activity> ) searchResults )
        {
            newTypes.add( GPlusListAdapter.getActivityType( activity ) );
        }
        if ( searchType == SearchType.NEWER )
        {
            resultTypes.addAll( 0, newTypes );
        }
        else
        {
            resultTypes.addAll( newTypes );
        }
    }

    public static int getActivityType( Activity activity )
    {
        int activityType = ACTIVITY_TYPE_NORMAL;
        String objectType = null == activity.getObject().getAttachments() ? "" : activity.getObject().getAttachments().get( 0 ).getObjectType();
        boolean hasMedia = ( "photo".equals( objectType ) || "video".equals( objectType ) );
        boolean hasLink = "article".equals( objectType );

        if ( hasMedia )
        {
            activityType = ACTIVITY_TYPE_MEDIA;
        }
        else if ( hasLink )
        {
            activityType = ACTIVITY_TYPE_LINK;
        }

        return activityType;
    }
}
