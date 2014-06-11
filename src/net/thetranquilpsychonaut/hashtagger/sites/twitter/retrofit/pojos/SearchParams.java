package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by itwenty on 6/8/14.
 */
public class SearchParams
{
    public static enum ResultType
    {
        mixed, recent, popular
    }

    private static final String MAX_ID_KEY      = "max_id";
    private static final String SINCE_ID_KEY    = "since_id";
    private static final String QUERY_KEY       = "q";
    private static final String RESULT_TYPE_KEY = "result_type";
    private static final String COUNT_KEY       = "count";

    private Map<String, String> params;

    public SearchParams( String query )
    {
        params = new HashMap<String, String>( 5 );
        params.put( QUERY_KEY, query );
    }

    public SearchParams setQuery( String query )
    {
        params.put( QUERY_KEY, query );
        return this;
    }

    public SearchParams setMaxId( String maxId )
    {
        params.put( MAX_ID_KEY, maxId );
        return this;
    }

    public SearchParams setSinceId( String sinceId )
    {
        params.put( SINCE_ID_KEY, sinceId );
        return this;
    }

    public SearchParams setResultType( ResultType resultType )
    {
        params.put( RESULT_TYPE_KEY, resultType.name() );
        return this;
    }

    public SearchParams setCount( int count )
    {
        params.put( COUNT_KEY, String.valueOf( count ) );
        return this;
    }

    public Map<String, String> getParams()
    {
        return params;
    }
}
