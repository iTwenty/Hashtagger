package net.thetranquilpsychonaut.hashtagger.events;

import org.scribe.model.Token;

/**
 * Created by itwenty on 6/23/14.
 */
public class FacebookAuthDoneEvent extends SitesAuthDoneEvent
{
    public FacebookAuthDoneEvent( boolean success, Token token, String userName )
    {
        super( success, token, userName );
    }
}
