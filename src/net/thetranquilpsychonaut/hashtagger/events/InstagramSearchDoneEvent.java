package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.SearchResult;

/**
 * Created by itwenty on 6/25/14.
 */
public class InstagramSearchDoneEvent extends SitesSearchDoneEvent
{
    private SearchResult searchResult;

    public InstagramSearchDoneEvent( int searchType, boolean success, SearchResult searchResult )
    {
        super( searchType, success );
        this.searchResult = searchResult;
    }

    public SearchResult getSearchResult()
    {
        return searchResult;
    }
}
