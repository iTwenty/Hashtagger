package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.List;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramListAdapter extends SitesListAdapter
{
    public static final  int MEDIA_TYPE_MEDIA = 0;
    private static final int MEDIA_TYPE_COUNT = 1;

    public InstagramListAdapter( Context context, int textViewResourceId, List<?> objects, List<Integer> resultTypes )
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
        return MEDIA_TYPE_COUNT;
    }

    @Override
    protected SitesListRow getSitesListRow( Context context, int position, View convertView, ViewGroup parent )
    {
        if ( null == convertView )
        {
            convertView = new InstagramListRow( context );
        }
        return ( SitesListRow ) convertView;
    }

    public static int getMediaType( Media media )
    {
        return MEDIA_TYPE_MEDIA;
    }
}
