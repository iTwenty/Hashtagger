package net.thetranquilpsychonaut.hashtagger.events;


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
