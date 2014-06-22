package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.CommentFeed;

/**
 * Created by itwenty on 6/15/14.
 */
public class CommentFeedEvent extends ResultEvent
{
    private CommentFeed commentFeed;

    public CommentFeedEvent( CommentFeed feed, boolean success )
    {
        super( success );
        this.commentFeed = feed;
    }

    public CommentFeed getCommentFeed()
    {
        return commentFeed;
    }
}
