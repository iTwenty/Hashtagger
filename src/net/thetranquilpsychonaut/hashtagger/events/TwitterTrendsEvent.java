package net.thetranquilpsychonaut.hashtagger.events;

import java.util.List;

/**
 * Created by itwenty on 6/3/14.
 */
public class TwitterTrendsEvent
{
    private List<String> trends;
    private String       place;
    private int          status;

    public TwitterTrendsEvent( List<String> trends, String place, int status )
    {
        this.trends = trends;
        this.place = place;
        this.status = status;
    }

    public List<String> getTrends()
    {
        return trends;
    }

    public String getPlace()
    {
        return place;
    }

    public int getStatus()
    {
        return status;
    }
}
