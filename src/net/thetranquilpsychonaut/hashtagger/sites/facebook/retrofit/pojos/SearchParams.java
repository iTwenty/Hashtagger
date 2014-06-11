package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by itwenty on 6/11/14.
 */
public class SearchParams
{
    private static final String QUERY_KEY = "q";
    private static final String LIMIT_KEY = "limit";
    private static final String UNTIL_KEY = "until";
    private static final String SINCE_KEY = "since";

    private Map<String, String> params;

    public SearchParams( String query )
    {
        params = new HashMap<String, String>();
        params.put( QUERY_KEY, query );
    }

    public SearchParams setQuery( String query )
    {
        params.put( QUERY_KEY, query );
        return this;
    }

    public SearchParams setLimit( int limit )
    {
        params.put( LIMIT_KEY, Integer.toString( limit ) );
        return this;
    }

    public SearchParams setUntil( String until )
    {
        params.put( UNTIL_KEY, until );
        return this;
    }

    public SearchParams setSince( String since )
    {
        params.put( SINCE_KEY, since );
        return this;
    }

    public Map<String, String> getParams()
    {
        return params;
    }
}
