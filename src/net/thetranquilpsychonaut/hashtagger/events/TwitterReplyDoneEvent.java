package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

/**
 * Created by itwenty on 5/17/14.
 */
public class TwitterReplyDoneEvent
{
    Status  status;
    boolean success;

    public TwitterReplyDoneEvent( boolean success, Status status )
    {
        this.success = success;
        this.status = status;
    }

    public boolean getSuccess()
    {
        return this.success;
    }

    public Status getStatus()
    {
        return status;
    }
}
