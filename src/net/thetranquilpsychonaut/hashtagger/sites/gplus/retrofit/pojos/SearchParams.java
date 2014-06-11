package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by itwenty on 6/11/14.
 */
public class SearchParams
{
    public static enum OrderBy
    {
        best, recent;
    }

    private static final String LANG_KEY        = "language";
    private static final String MAX_RESULTS_KEY = "maxResults";
    private static final String QUERY_KEY       = "query";
    private static final String ORBER_BY_KEY    = "orderBy";
    private static final String PAGE_TOKEN_KEY  = "pageToken";

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

    public SearchParams setLanguage( String language )
    {
        params.put( LANG_KEY, language );
        return this;
    }

    public SearchParams setMaxResults( int maxResults )
    {
        params.put( MAX_RESULTS_KEY, Integer.toString( maxResults ) );
        return this;
    }

    public SearchParams setOrderBy( OrderBy orderBy )
    {
        params.put( ORBER_BY_KEY, orderBy.name() );
        return this;
    }

    public SearchParams setPageToken( String pageToken )
    {
        params.put( PAGE_TOKEN_KEY, pageToken );
        return this;
    }

    public Map<String, String> getParams()
    {
        return params;
    }
}
