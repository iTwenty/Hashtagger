package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import net.thetranquilpsychonaut.hashtagger.sites.retrofit.MapParams;

/**
 * Created by itwenty on 6/11/14.
 */
public class SearchParams extends MapParams
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

    public SearchParams setLanguage( String language )
    {
        getParams().put( LANG_KEY, language );
        return this;
    }

    public SearchParams setMaxResults( int maxResults )
    {
        getParams().put( MAX_RESULTS_KEY, Integer.toString( maxResults ) );
        return this;
    }

    public SearchParams setOrderBy( OrderBy orderBy )
    {
        getParams().put( ORBER_BY_KEY, orderBy.name() );
        return this;
    }

    public SearchParams setPageToken( String pageToken )
    {
        getParams().put( PAGE_TOKEN_KEY, pageToken );
        return this;
    }
}
