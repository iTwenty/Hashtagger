package net.thetranquilpsychonaut.hashtagger.events;

import twitter4j.Status;

/**
 * Created by itwenty on 5/17/14.
 */
public class TwitterRetweetEvent
{
    int     position;
    Status  status;
    boolean success;

    public TwitterRetweetEvent( boolean success, int position, Status status )
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
