package net.thetranquilpsychonaut.hashtagger.events;


import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

/**
 * Created by itwenty on 5/17/14.
 */
public class TwitterRetweetEvent
{
    int     position;
    boolean success;

    public TwitterRetweetEvent( boolean success, int position )
    {
        this.position = position;
        this.success = success;
    }

    public int getPosition()
    {
        return position;
    }

    public boolean getSuccess()
    {
        return success;
    }
}
