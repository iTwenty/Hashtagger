package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;

/**
 * Created by itwenty on 6/14/14.
 */
public class FacebookActionClickedEvent
{
    public static final int ACTION_LIKE    = 0;
    public static final int ACTION_COMMENT = 1;
    public static final int ACTION_SHARE   = 2;

    private Post post;
    private int  actionType;

    public FacebookActionClickedEvent( Post post, int actionType )
    {
        this.post = post;
        this.actionType = actionType;
    }

    public Post getPost()
    {
        return post;
    }

    public int getActionType()
    {
        return actionType;
    }
}
