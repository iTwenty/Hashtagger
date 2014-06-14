package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import net.thetranquilpsychonaut.hashtagger.sites.retrofit.MapParams;

/**
 * Created by itwenty on 6/11/14.
 */
public class SearchParams extends MapParams
{
    private static final String QUERY_KEY = "q";
    private static final String LIMIT_KEY = "limit";
    private static final String UNTIL_KEY = "until";
    private static final String SINCE_KEY = "since";

    public SearchParams( String query )
    {
        super();
        getParams().put( QUERY_KEY, query );
    }

    public SearchParams setQuery( String query )
    {
        getParams().put( QUERY_KEY, query );
        return this;
    }

    public SearchParams setLimit( int limit )
    {
        getParams().put( LIMIT_KEY, Integer.toString( limit ) );
        return this;
    }

    public SearchParams setUntil( String until )
    {
        getParams().put( UNTIL_KEY, until );
        return this;
    }

    public SearchParams setSince( String since )
    {
        getParams().put( SINCE_KEY, since );
        return this;
    }
}
