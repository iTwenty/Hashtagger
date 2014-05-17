package net.thetranquilpsychonaut.hashtagger.events;

/**
 * Created by itwenty on 5/17/14.
 */
public class TwitterReplyEvent
{
    boolean success;

    public TwitterReplyEvent( boolean success )
    {
        this.success = success;
    }

    public boolean getSuccess()
    {
        return this.success;
    }
}
