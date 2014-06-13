package net.thetranquilpsychonaut.hashtagger.events;

/**
 * Created by itwenty on 6/13/14.
 */
public abstract class SitesActionClickedEvent
{
    private boolean shouldShowDialog;
    private int position;

    public SitesActionClickedEvent( boolean shouldShowDialog, int position )
    {
        this.shouldShowDialog = shouldShowDialog;
        this.position = position;
    }

    public boolean shouldShowDialog()
    {
        return shouldShowDialog;
    }

    public int getPosition()
    {
        return position;
    }
}
