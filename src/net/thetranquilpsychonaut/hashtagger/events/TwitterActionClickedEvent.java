package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

/**
 * Created by itwenty on 6/14/14.
 */
public class TwitterActionClickedEvent
{
    public static final int ACTION_REPLY    = 0;
    public static final int ACTION_RETWEET  = 1;
    public static final int ACTION_FAVORITE = 2;

    private Status status;
    private int    actionType;

    public TwitterActionClickedEvent( Status status, int actionType )
    {
        this.status = status;
        this.actionType = actionType;
    }

    public Status getStatus()
    {
        return status;
    }

    public int getActionType()
    {
        return actionType;
    }
}
