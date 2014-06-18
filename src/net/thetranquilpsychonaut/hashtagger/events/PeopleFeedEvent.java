package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.PeopleFeed;

/**
 * Created by itwenty on 6/14/14.
 */
public class PeopleFeedEvent
{
    private PeopleFeed peopleFeed;
    private boolean    success;
    private String     collection;

    public PeopleFeedEvent( PeopleFeed peopleFeed, boolean success, String collection )
    {
        this.peopleFeed = peopleFeed;
        this.success = success;
        this.collection = collection;
    }

    public PeopleFeed getPeopleFeed()
    {
        return peopleFeed;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getCollection()
    {
        return collection;
    }
}
