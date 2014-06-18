package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit;

import net.thetranquilpsychonaut.hashtagger.config.GPlusConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSigningClient;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.Google2Api;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import java.io.IOException;

/**
 * Created by itwenty on 6/10/14.
 */
public class GPlusSigningClient extends SitesSigningClient
{
    @Override
    protected String getScope()
    {
        return GPlusConfig.GPLUS_ACCESS_SCOPE;
    }

    @Override
    protected String getApiSecret()
    {
        return GPlusConfig.GPLUS_OAUTH_CLIENT_SECRET;
    }

    @Override
    protected String getApiKey()
    {
        return GPlusConfig.GPLUS_OAUTH_CLIENT_ID;
    }

    @Override
    protected Class<? extends Api> getProviderClass()
    {
        return Google2Api.class;
    }

    @Override
    protected boolean accessTokenCanExpire()
    {
        return true;
    }

    @Override
    protected Token getAccessToken()
    {
        return new Token( AccountPrefs.getGPlusAccessToken(), "" );
    }

    @Override
    protected boolean isUserLoggedIn()
    {
        return AccountPrefs.areGPlusDetailsPresent();
    }

    @Override
    protected void updateAccessToken() throws IOException
    {
        Helper.debug( "Google access token has expired. Refreshing..." );
        OAuthRequest request = new OAuthRequest( Verb.POST, "https://accounts.google.com/o/oauth2/token" );
        request.addBodyParameter( "refresh_token", AccountPrefs.getGPlusRefreshToken() );
        request.addBodyParameter( "client_id", GPlusConfig.GPLUS_OAUTH_CLIENT_ID );
        request.addBodyParameter( "client_secret", GPlusConfig.GPLUS_OAUTH_CLIENT_SECRET );
        request.addBodyParameter( "grant_type", "refresh_token" );
        Response response = request.send();
        if ( response.getCode() == 200 )
        {
            String accessToken = Helper.extractJsonStringField( response.getBody(), "access_token" );
            AccountPrefs.updateGPlusAccessToken( accessToken );
            Helper.debug( "Google access token updated and stored successfully" );
        }
        else
        {
            String failure = "Failed to update google access token";
            Helper.debug( failure );
            throw new IOException( failure );
        }
    }
}
