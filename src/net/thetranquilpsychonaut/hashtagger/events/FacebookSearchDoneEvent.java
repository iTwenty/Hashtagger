package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.SearchResult;

/**
 * Created by itwenty on 6/23/14.
 */
public class FacebookSearchDoneEvent extends SitesSearchDoneEvent
{
    private SearchResult searchResult;

    public FacebookSearchDoneEvent( int searchType, boolean success, SearchResult searchResult )
    {
        super( searchType, success );
        this.searchResult = searchResult;
    }

    public SearchResult getSearchResult()
    {
        return this.searchResult;
    }
}
