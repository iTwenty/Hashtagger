package net.thetranquilpsychonaut.hashtagger.events;

/**
 * Created by itwenty on 6/23/14.
 */
public class SitesSearchDoneEvent extends ResultEvent
{
    int searchType;

    public SitesSearchDoneEvent( int searchType, boolean success )
    {
        super( success );
        this.searchType = searchType;
    }

    public int getSearchType()
    {
        return this.searchType;
    }
}
