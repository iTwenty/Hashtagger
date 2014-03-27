package net.thetranquilpsychonaut.hashtagger.ui.twitter;

import twitter4j.Status;

import java.util.List;

/**
 * Created by itwenty on 3/13/14.
 */
public interface TwitterSearchHandlerListener
{
    public void whileSearching();

    public void afterSearching( List<Status> statuses );

    public void onError();
}
