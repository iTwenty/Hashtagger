package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.From;

import java.util.List;

/**
 * Created by itwenty on 7/5/14.
 */
public class InstagramLikesListEvent extends ResultEvent
{
    private List<From> likes;

    public InstagramLikesListEvent( boolean success, List<From> likes )
    {
        super( success );
        this.likes = likes;
    }

    public List<From> getLikes()
    {
        return likes;
    }
}
