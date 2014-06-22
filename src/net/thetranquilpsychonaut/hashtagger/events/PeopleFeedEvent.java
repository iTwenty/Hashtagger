package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.PeopleFeed;

/**
 * Created by itwenty on 6/14/14.
 */
public class PeopleFeedEvent extends ResultEvent
{
    private PeopleFeed peopleFeed;
    private String     collection;

    public PeopleFeedEvent( PeopleFeed peopleFeed, boolean success, String collection )
    {
        super( success );
        this.peopleFeed = peopleFeed;
        this.collection = collection;
    }

    public PeopleFeed getPeopleFeed()
    {
        return peopleFeed;
    }

    public String getCollection()
    {
        return collection;
    }
}
