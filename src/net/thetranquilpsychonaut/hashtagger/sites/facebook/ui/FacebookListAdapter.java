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
    public static final int POST_TYPE_MEDIA  = 1;
    public static final int POST_TYPE_LINK   = 2;
    public static final int POST_TYPE_COUNT  = 3;

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
            case POST_TYPE_LINK:
                if ( null == convertView || !( convertView instanceof FacebookLinkRow ) )
                {
                    convertView = new FacebookLinkRow( context );
                }
                break;
        }
        return ( SitesListRow ) convertView;
    }

    public static int getPostType( Post post )
    {
        int postType = POST_TYPE_NORMAL;
        boolean hasMedia = ( "video".equals( post.getType() ) || "photo".equals( post.getType() ) );
        boolean hasLink = "link".equals( post.getType() );

        if ( hasMedia )
        {
            postType = POST_TYPE_MEDIA;
        }
        else if ( hasLink )
        {
            postType = POST_TYPE_LINK;
        }

        return postType;
    }
}
