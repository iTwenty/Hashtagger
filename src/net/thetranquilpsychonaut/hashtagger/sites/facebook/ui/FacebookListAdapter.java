package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookListAdapter extends SitesListAdapter
{
    public static final int POST_TYPE_NORMAL = 0;
    public static final int POST_TYPE_PHOTO  = 1;
    public static final int POST_TYPE_VIDEO  = 2;
    public static final int POST_TYPE_LINK   = 3;
    public static final int POST_TYPE_COUNT  = 4;

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
            case POST_TYPE_PHOTO:
                if ( null == convertView || !( convertView instanceof FacebookPhotoRow ) )
                {
                    convertView = new FacebookPhotoRow( context );
                }
                break;
            case POST_TYPE_VIDEO:
                if ( null == convertView || !( convertView instanceof FacebookDetailRow ) )
                {
                    convertView = new FacebookDetailRow( context );
                }
                break;
            case POST_TYPE_LINK:
                if ( null == convertView || !( convertView instanceof FacebookDetailRow ) )
                {
                    convertView = new FacebookDetailRow( context );
                }
                break;
        }
        return ( SitesListRow ) convertView;
    }

    public static int getPostType( Post post )
    {
        int postType = POST_TYPE_NORMAL;

        if ( "photo".equals( post.getType() ) )
        {
            postType = POST_TYPE_PHOTO;
        }
        else if ( "video".equals( post.getType() ) )
        {
            postType = POST_TYPE_VIDEO;
        }
        else if ( "link".equals( post.getType() ) )
        {
            postType = POST_TYPE_LINK;
        }

        return postType;
    }
}
