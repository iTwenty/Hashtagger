package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

import java.util.List;

/**
 * Created by itwenty on 7/3/14.
 */
public class TwitterRetweetListEvent extends ResultEvent
{
    private List<Status> retweets;

    public TwitterRetweetListEvent( List<Status> retweets, boolean success )
    {
        super( success );
        this.retweets = retweets;
    }

    public List<Status> getRetweets()
    {
        return this.retweets;
    }
}
