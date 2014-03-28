package net.thetranquilpsychonaut.hashtagger.ui.twitter;

import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import twitter4j.Status;

import java.util.List;

/**
 * Created by itwenty on 3/13/14.
 */
public interface TwitterSearchHandlerListener
{
    public void whileSearching( SearchType searchType );

    public void afterSearching( SearchType searchType, List<Status> statuses );

    public void onError( SearchType searchType );
}
