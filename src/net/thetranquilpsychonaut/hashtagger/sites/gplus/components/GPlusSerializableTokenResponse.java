package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

import java.io.Serializable;

/**
 * Created by itwenty on 5/6/14.
 */
public class GPlusSerializableTokenResponse implements Serializable
{
    private String accessToken;
    private String refreshToken;

    public GPlusSerializableTokenResponse( GoogleTokenResponse response )
    {
        this.accessToken = response.getAccessToken();
        this.refreshToken = response.getRefreshToken();
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setRefreshToken( String refreshToken )
    {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken( String accessToken )
    {
        this.accessToken = accessToken;
    }
}
