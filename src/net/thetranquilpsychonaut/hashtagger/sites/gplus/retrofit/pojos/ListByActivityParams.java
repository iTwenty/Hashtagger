package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import net.thetranquilpsychonaut.hashtagger.sites.retrofit.MapParams;

/**
 * Created by itwenty on 6/14/14.
 */
public class ListByActivityParams extends MapParams
{
    public static final  String PLUSONERS       = "plusoners";
    public static final  String RESHARERS       = "resharers";
    private static final String MAX_RESULTS_KEY = "maxResults";
    private static final String PAGE_TOKEN_KEY  = "pageToken";

    public ListByActivityParams setMaxResults( int maxResults )
    {
        getParams().put( MAX_RESULTS_KEY, Integer.toString( maxResults ) );
        return this;
    }

    public ListByActivityParams setPageToken( String pageToken )
    {
        getParams().put( PAGE_TOKEN_KEY, pageToken );
        return this;
    }
}
