package net.thetranquilpsychonaut.hashtagger.events;

/**
 * Created by itwenty on 6/13/14.
 */
public class PlusOneClickedEvent extends SitesActionClickedEvent
{
    public PlusOneClickedEvent( int position )
    {
        super( true, position );
    }
}
