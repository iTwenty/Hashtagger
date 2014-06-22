package net.thetranquilpsychonaut.hashtagger.events;

/**
 * Created by itwenty on 6/23/14.
 */
public class ResultEvent
{
    private boolean success;

    public ResultEvent( boolean success )
    {
        this.success = success;
    }

    public boolean isSuccess()
    {
        return this.success;
    }
}
