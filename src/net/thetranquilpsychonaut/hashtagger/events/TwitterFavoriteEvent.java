package net.thetranquilpsychonaut.hashtagger.events;

/**
 * Created by itwenty on 5/17/14.
 */
public class TwitterFavoriteEvent
{
    int     position;
    boolean wasFavorited;
    boolean success;

    public TwitterFavoriteEvent( boolean success, int position, boolean wasFavorited )
    {
        this.position = position;
        this.success = success;
        this.wasFavorited = wasFavorited;
    }

    public int getPosition()
    {
        return position;
    }

    public boolean getSuccess()
    {
        return success;
    }

    public boolean wasFavorited()
    {
        return wasFavorited;
    }
}
