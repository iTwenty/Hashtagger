package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.Helper;
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
    private static final String POST_TYPES_KEY = "post_types_key";

    public static final int POST_TYPE_NORMAL           = 0;
    public static final int POST_TYPE_MEDIA            = 1;
    public static final int POST_TYPE_MEDIA_NO_MESSAGE = 2;
    public static final int POST_TYPE_LINK             = 3;
    public static final int POST_TYPE_COUNT            = 4;

    private List<Integer> postTypes;

    public FacebookListAdapter( Context context, int textViewResourceId, List<?> posts )
    {
        super( context, textViewResourceId, posts );
    }

    @Override
    public int getItemViewType( int position )
    {
        return postTypes.get( position );
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
                    Helper.debug( "new fb normal row" );
                }
                break;
            case POST_TYPE_MEDIA:
                if ( null == convertView || !( convertView instanceof FacebookMediaRow ) )
                {
                    convertView = new FacebookMediaRow( context );
                    Helper.debug( "new fb media row" );
                }
                break;
            case POST_TYPE_MEDIA_NO_MESSAGE:
                if ( null == convertView || !( convertView instanceof FacebookMediaNoMessageRow ) )
                {
                    convertView = new FacebookMediaNoMessageRow( context );
                    Helper.debug( "new fb media no message row" );
                }
                break;
            case POST_TYPE_LINK:
                if ( null == convertView || !( convertView instanceof FacebookLinkRow ) )
                {
                    convertView = new FacebookLinkRow( context );
                    Helper.debug( "new fb link row" );
                }
                break;
        }
        return ( SitesListRow ) convertView;
    }

    @Override
    public void updateTypes( SearchType searchType, List<?> searchResults )
    {
        List<Integer> newTypes = new ArrayList<Integer>( searchResults.size() );
        boolean hasMessage;
        boolean hasMedia;
        boolean hasLink;
        String postType;
        for ( Post post : ( List<Post> ) searchResults )
        {
            hasMessage = null == post.getMessage();
            postType = post.getType();
            hasMedia = ( "video".equals( postType ) || "photo".equals( postType ) );
            hasLink = "link".equals( postType );
            if ( hasMedia && hasMessage )
            {
                newTypes.add( POST_TYPE_MEDIA );
            }
            else if ( hasMedia && !hasMessage )
            {
                newTypes.add( POST_TYPE_MEDIA_NO_MESSAGE );
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
            postTypes.addAll( 0, newTypes );
        }
        else
        {
            postTypes.addAll( newTypes );
        }
    }

    @Override
    public void clearTypes()
    {
        postTypes.clear();
    }

    @Override
    public void initTypes( Bundle savedInstanceState )
    {
        if ( null != savedInstanceState )
        {
            postTypes = ( List<Integer> ) savedInstanceState.getSerializable( POST_TYPES_KEY );
        }
        else
        {
            postTypes = new ArrayList<Integer>();
        }
    }

    @Override
    public void saveTypes( Bundle outState )
    {
        outState.putSerializable( POST_TYPES_KEY, ( java.io.Serializable ) postTypes );
    }
}
