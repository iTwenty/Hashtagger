package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Comment;

import java.util.List;

/**
 * Created by itwenty on 7/5/14.
 */
public class InstagramCommentsListEvent extends ResultEvent
{
    private List<Comment> comments;

    public InstagramCommentsListEvent( boolean success, List<Comment> comments )
    {
        super( success );
        this.comments = comments;
    }

    public List<Comment> getComments()
    {
        return comments;
    }
}
