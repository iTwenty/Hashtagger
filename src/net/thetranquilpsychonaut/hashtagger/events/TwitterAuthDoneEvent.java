package net.thetranquilpsychonaut.hashtagger.events;

import org.scribe.model.Token;

/**
 * Created by itwenty on 6/23/14.
 */
public class TwitterAuthDoneEvent extends SitesAuthDoneEvent
{
    // authUrl is present only when this event is sent for request auth.
    private int    authType;
    private String authUrl;

    public TwitterAuthDoneEvent( boolean success, Token token, String userName, String authUrl, int authType )
    {
        super( success, token, userName );
        this.authUrl = authUrl;
        this.authType = authType;
    }

    public String getAuthUrl()
    {
        return this.authUrl;
    }

    public int getAuthType()
    {
        return authType;
    }
}
