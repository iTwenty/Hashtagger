package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookListAdapter extends SitesListAdapter
{
    public static final int POST_TYPE_NORMAL = 0;
    public static final int POST_TYPE_MEDIA  = 1;
    public static final int POST_TYPE_COUNT  = 2;

    protected FacebookListAdapter( Context context, int textViewResourceId, List<?> objects, List<Integer> resultTypes )
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
        return POST_TYPE_COUNT;
    }

    @Override
    protected SitesListRow getSitesListRow( Context context, int position, View convertView, ViewGroup parent )
    {
        switch ( getItemViewType( position ) )
        {
            case POST_TYPE_NORMAL:
                if ( null == convertView || !( convertView instanceof FacebookNormalRow ) )
                {
                    convertView = new FacebookNormalRow( context );
                }
                break;
            case POST_TYPE_MEDIA:
                if ( null == convertView || !( convertView instanceof FacebookMediaRow ) )
                {
                    convertView = new FacebookMediaRow( context );
                }
                break;
        }
        return ( SitesListRow ) convertView;
    }

    public static int getPostType( Post post )
    {
        int postType = POST_TYPE_NORMAL;

        if ( "photo".equals( post.getType() ) || "video".equals( post.getType() ) )
        {
            postType = POST_TYPE_MEDIA;
        }

        return postType;
    }
}
