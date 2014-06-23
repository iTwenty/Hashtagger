package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

/**
 * Created by itwenty on 5/17/14.
 */
public class TwitterFavoriteDoneEvent extends ResultEvent
{
    Status status;

    public TwitterFavoriteDoneEvent( boolean success, Status status )
    {
        super( success );
        this.status = status;
    }

    public Status getStatus()
    {
        return status;
    }
}
