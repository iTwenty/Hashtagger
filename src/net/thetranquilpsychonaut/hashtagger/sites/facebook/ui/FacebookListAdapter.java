package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookListAdapter extends SitesListAdapter
{
    private static final int POST_TYPE_NORMAL = 0;
    private static final int POST_TYPE_MEDIA  = 1;
    private static final int POST_TYPE_LINK   = 2;
    private static final int POST_TYPE_COUNT  = 3;

    public FacebookListAdapter( Context context, int textViewResourceId, List<?> posts )
    {
        super( context, textViewResourceId, posts );
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

    @Override
    public void updateTypes( SearchType searchType, List<?> searchResults )
    {
        if ( searchResults.isEmpty() )
        {
            return;
        }

        List<Integer> newTypes = new ArrayList<Integer>( searchResults.size() );
        boolean hasMedia;
        boolean hasLink;
        String postType;
        for ( Post post : ( List<Post> ) searchResults )
        {
            postType = post.getType();
            hasMedia = ( "video".equals( postType ) || "photo".equals( postType ) );
            hasLink = "link".equals( postType );
            if ( hasMedia )
            {
                newTypes.add( POST_TYPE_MEDIA );
            }
            else if ( hasLink )
            {
                newTypes.add( POST_TYPE_LINK );
            }
            else
            {
                newTypes.add( POST_TYPE_NORMAL );
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
