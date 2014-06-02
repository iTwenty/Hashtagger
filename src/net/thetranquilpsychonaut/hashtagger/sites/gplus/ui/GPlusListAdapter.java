package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.List;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusListAdapter extends SitesListAdapter
{
    public static final int ACTIVITY_TYPE_NORMAL = 0;
    public static final int ACTIVITY_TYPE_PHOTO  = 1;
    public static final int ACTIVITY_TYPE_VIDEO  = 2;
    public static final int ACTIVITY_TYPE_LINK   = 3;
    public static final int ACTIVITY_TYPE_ALBUM  = 4;
    public static final int ACTIVITY_TYPE_COUNT  = 5;

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
            case ACTIVITY_TYPE_PHOTO:
                if ( null == convertView || !( convertView instanceof GPlusPhotoRow ) )
                {
                    convertView = new GPlusPhotoRow( context );
                }
                break;
            case ACTIVITY_TYPE_VIDEO:
                if ( null == convertView || !( convertView instanceof GPlusDetailRow ) )
                {
                    convertView = new GPlusDetailRow( context );
                }
                break;
            case ACTIVITY_TYPE_LINK:
                if ( null == convertView || !( convertView instanceof GPlusDetailRow ) )
                {
                    convertView = new GPlusDetailRow( context );
                }
                break;
            case ACTIVITY_TYPE_ALBUM:
                if ( null == convertView || !( convertView instanceof GPlusAlbumRow ) )
                {
                    convertView = new GPlusAlbumRow( context );
                }
        }
        return ( SitesListRow ) convertView;
    }

    public static int getActivityType( Activity activity )
    {
        int activityType = ACTIVITY_TYPE_NORMAL;
        String objectType = null == activity.getObject().getAttachments() ?
                "" :
                activity.getObject().getAttachments().get( 0 ).getObjectType();

        if ( "photo".equals( objectType ) )
        {
            activityType = ACTIVITY_TYPE_PHOTO;
        }
        else if ( "video".equals( objectType ) )
        {
            activityType = ACTIVITY_TYPE_VIDEO;
        }
        else if ( "article".equals( objectType ) )
        {
            activityType = ACTIVITY_TYPE_LINK;
        }
        else if ( "album".equals( objectType ) )
        {
            activityType = ACTIVITY_TYPE_ALBUM;
        }

        return activityType;
    }
}
