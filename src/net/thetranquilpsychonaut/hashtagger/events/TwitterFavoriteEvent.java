package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

/**
 * Created by itwenty on 5/17/14.
 */
public class TwitterFavoriteEvent
{
    int     position;
    Status  status;
    boolean success;

    public TwitterFavoriteEvent( boolean success, int position, Status status )
    {
        this.position = position;
        this.status = status;
        this.success = success;
    }

    public int getPosition()
    {
        return position;
    }

    public Status getStatus()
    {
        return status;
    }

    public boolean getSuccess()
    {
        return success;
    }
}
