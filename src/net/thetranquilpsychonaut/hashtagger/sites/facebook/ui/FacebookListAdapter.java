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

    public static final int POST_TYPE_NORMAL            = 0;
    public static final int POST_TYPE_OBJECT            = 1;
    public static final int POST_TYPE_DETAILS           = 2;
    public static final int POST_TYPE_OBJECT_NO_MESSAGE = 3;
    public static final int POST_TYPE_COUNT             = 4;

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
            case POST_TYPE_OBJECT:
                if ( null == convertView || !( convertView instanceof FacebookObjectRow ) )
                {
                    convertView = new FacebookObjectRow( context );
                    Helper.debug( "new fb object row" );
                }
                break;
            case POST_TYPE_DETAILS:
                if ( null == convertView || !( convertView instanceof FacebookDetailsRow ) )
                {
                    convertView = new FacebookDetailsRow( context );
                    Helper.debug( "new fb details row" );
                }
                break;
            case POST_TYPE_OBJECT_NO_MESSAGE:
                if ( null == convertView || !( convertView instanceof FacebookObjectNoMessageRow ) )
                {
                    convertView = new FacebookObjectNoMessageRow( context );
                    Helper.debug( "new fb object no message row" );
                }
                break;
        }
        return ( SitesListRow ) convertView;
    }

    @Override
    public void updateTypes( SearchType searchType, List<?> searchResults )
    {
        List<Integer> newTypes = new ArrayList<Integer>( searchResults.size() );
        boolean hasObjectId;
        boolean isMessageEmpty;
        boolean isTypeStatus;
        for ( Post post : ( List<Post> ) searchResults )
        {
            hasObjectId = null != post.getObjectId();
            isMessageEmpty = null == post.getMessage();
            isTypeStatus = "status".equals( post.getType() );
            if ( hasObjectId && isMessageEmpty )
            {
                newTypes.add( POST_TYPE_OBJECT_NO_MESSAGE );
            }
            else if ( hasObjectId )
            {
                newTypes.add( POST_TYPE_OBJECT );
            }
            else if ( !isTypeStatus )
            {
                newTypes.add( POST_TYPE_DETAILS );
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
