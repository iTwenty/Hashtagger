package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.CommentFeed;

/**
 * Created by itwenty on 6/15/14.
 */
public class CommentFeedEvent
{
    private CommentFeed commentFeed;
    private boolean     success;

    public CommentFeedEvent( CommentFeed feed, boolean success )
    {
        this.commentFeed = feed;
        this.success = success;
    }

    public CommentFeed getCommentFeed()
    {
        return commentFeed;
    }

    public boolean isSuccess()
    {
        return success;
    }
}
