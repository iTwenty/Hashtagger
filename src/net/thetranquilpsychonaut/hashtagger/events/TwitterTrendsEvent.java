package net.thetranquilpsychonaut.hashtagger.events;

import java.util.List;

/**
 * Created by itwenty on 6/3/14.
 */
public class TwitterTrendsEvent
{
    private List<String> trends;
    private int          trendingChoice;
    private int          status;

    public TwitterTrendsEvent( List<String> trends, int trendingChoice, int status )
    {
        this.trends = trends;
        this.trendingChoice = trendingChoice;
        this.status = status;
    }

    public List<String> getTrends()
    {
        return trends;
    }

    public int getTrendingChoice()
    {
        return trendingChoice;
    }

    public int getStatus()
    {
        return status;
    }
}
