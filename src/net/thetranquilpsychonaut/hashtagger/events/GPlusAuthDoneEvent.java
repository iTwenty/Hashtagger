package net.thetranquilpsychonaut.hashtagger.events;

import org.scribe.model.Token;

/**
 * Created by itwenty on 6/23/14.
 */
public class GPlusAuthDoneEvent extends SitesAuthDoneEvent
{
    public GPlusAuthDoneEvent( boolean success, Token token, String userName )
    {
        super( success, token, userName );
    }
}
