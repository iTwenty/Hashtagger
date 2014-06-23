package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.SearchResult;

/**
 * Created by itwenty on 6/23/14.
 */
public class TwitterSearchDoneEvent extends SitesSearchDoneEvent
{
    SearchResult searchResult;

    public TwitterSearchDoneEvent( int searchType, boolean success, SearchResult searchResult )
    {
        super( searchType, success );
        this.searchResult = searchResult;
    }

    public SearchResult getSearchResult()
    {
        return this.searchResult;
    }
}
