package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import net.thetranquilpsychonaut.hashtagger.sites.retrofit.MapParams;

/**
 * Created by itwenty on 6/15/14.
 */
public class ListCommentsParams extends MapParams
{
    public static enum SortOrder
    {
        ascending, descending;
    }

    private static final String MAX_RESULTS_KEY = "maxResults";
    private static final String PAGE_TOKEN_KEY  = "pageToken";
    private static final String SORT_ORDER_KEY  = "sortOrder";

    public ListCommentsParams setMaxResults( int maxResults )
    {
        getParams().put( MAX_RESULTS_KEY, Integer.toString( maxResults ) );
        return this;
    }

    public ListCommentsParams setPageToken( String pageToken )
    {
        getParams().put( PAGE_TOKEN_KEY, pageToken );
        return this;
    }

    public ListCommentsParams setSortOrder( SortOrder sortOrder )
    {
        getParams().put( SORT_ORDER_KEY, sortOrder.name() );
        return this;
    }
}
