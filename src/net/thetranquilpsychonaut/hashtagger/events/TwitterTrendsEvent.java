package net.thetranquilpsychonaut.hashtagger.events;

import twitter4j.Trends;

/**
 * Created by itwenty on 6/3/14.
 */
public class TwitterTrendsEvent
{
    private Trends trends;
    private String place;
    private int    code;

    public TwitterTrendsEvent( Trends trends, String place, int code )
    {
        this.trends = trends;
        this.place = place;
        this.code = code;
    }

    public Trends getTrends()
    {
        return trends;
    }

    public String getPlace()
    {
        return place;
    }

    public int getCode()
    {
        return code;
    }
}
