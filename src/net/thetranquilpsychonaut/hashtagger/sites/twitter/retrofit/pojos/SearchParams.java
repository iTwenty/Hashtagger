package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import net.thetranquilpsychonaut.hashtagger.sites.retrofit.MapParams;

/**
 * Created by itwenty on 6/8/14.
 */
public class SearchParams extends MapParams
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

    public SearchParams setMaxId( String maxId )
    {
        getParams().put( MAX_ID_KEY, maxId );
        return this;
    }

    public SearchParams setSinceId( String sinceId )
    {
        getParams().put( SINCE_ID_KEY, sinceId );
        return this;
    }

    public SearchParams setResultType( ResultType resultType )
    {
        getParams().put( RESULT_TYPE_KEY, resultType.name() );
        return this;
    }

    public SearchParams setCount( int count )
    {
        getParams().put( COUNT_KEY, String.valueOf( count ) );
        return this;
    }
}
