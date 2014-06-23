package net.thetranquilpsychonaut.hashtagger.events;

import org.scribe.model.Token;

/**
 * Created by itwenty on 6/23/14.
 */
public class SitesAuthDoneEvent extends ResultEvent
{
    private Token  token;
    private String userName;

    public SitesAuthDoneEvent( boolean success, Token token, String userName )
    {
        super( success );
        this.token = token;
        this.userName = userName;
    }

    public Token getToken()
    {
        return token;
    }

    public String getUserName()
    {
        return userName;
    }
}
