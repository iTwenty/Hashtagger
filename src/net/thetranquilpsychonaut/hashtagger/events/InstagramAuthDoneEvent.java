package net.thetranquilpsychonaut.hashtagger.events;

import org.scribe.model.Token;

/**
 * Created by itwenty on 6/25/14.
 */
public class InstagramAuthDoneEvent extends SitesAuthDoneEvent
{
    public InstagramAuthDoneEvent( boolean success, Token accessToken, String userName )
    {
        super( success, accessToken, userName );
    }
}
