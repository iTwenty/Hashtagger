package net.thetranquilpsychonaut.hashtagger.otto;

/**
 * Created by itwenty on 3/18/14.
 */
public class OAuthVerifierEvent
{
    private final String oauthVerifier;

    public OAuthVerifierEvent( String oauthVerifier )
    {
        this.oauthVerifier = oauthVerifier;
    }

    public String getOauthVerifier()
    {
        return this.oauthVerifier;
    }
}
