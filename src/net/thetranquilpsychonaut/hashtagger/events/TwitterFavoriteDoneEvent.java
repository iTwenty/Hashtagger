package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

/**
 * Created by itwenty on 5/17/14.
 */
public class TwitterFavoriteDoneEvent
{
    Status  status;
    boolean success;

    public TwitterFavoriteDoneEvent( boolean success, Status status )
    {
        this.success = success;
        this.status = status;
    }

    public boolean getSuccess()
    {
        return success;
    }

    public Status getStatus()
    {
        return status;
    }
}
