package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;

/**
 * Created by itwenty on 6/13/14.
 */
public class GPlusActionClickedEvent
{
    public static final int ACTION_PLUS_ONE = 0;
    public static final int ACTION_RESHARE  = 1;
    public static final int ACTION_REPLY    = 2;

    private Activity activity;
    private int      actionType;

    public GPlusActionClickedEvent( Activity activity, int actionType )
    {
        this.activity = activity;
        this.actionType = actionType;
    }

    public Activity getActivity()
    {
        return activity;
    }

    public int getActionType()
    {
        return actionType;
    }
}
