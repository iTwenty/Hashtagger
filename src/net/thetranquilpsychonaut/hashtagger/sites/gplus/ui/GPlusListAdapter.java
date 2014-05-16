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
    private static final int ACTIVITY_TYPE_NORMAL = 0;
    private static final int ACTIVITY_TYPE_MEDIA  = 1;
    private static final int ACTIVITY_TYPE_COUNT  = 2;

    public GPlusListAdapter( Context context, int textViewResourceId, List<?> objects )
    {
        super( context, textViewResourceId, objects );
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
        }
        return ( SitesListRow ) convertView;
    }

    @Override
    public void updateTypes( SearchType searchType, List<?> searchResults )
    {
        List<Integer> newTypes = new ArrayList<Integer>( searchResults.size() );
        boolean hasMedia;
        for ( Activity activity : ( List<Activity> ) searchResults )
        {
            hasMedia = null != activity.getObject().getAttachments()
                    && ( "photo".equals( activity.getObject().getAttachments().get( 0 ).getObjectType() )
                    || "video".equals( activity.getObject().getAttachments().get( 0 ).getObjectType() ) );

            if ( hasMedia )
            {
                newTypes.add( ACTIVITY_TYPE_MEDIA );
            }
            else
            {
                newTypes.add( ACTIVITY_TYPE_NORMAL );
            }
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
}
