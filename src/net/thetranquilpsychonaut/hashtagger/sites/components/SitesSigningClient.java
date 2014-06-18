package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.text.TextUtils;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by itwenty on 6/9/14.
 */
public abstract class SitesSigningClient extends OkClient
{
    private OAuthService signingService;

    public SitesSigningClient()
    {
        super();
        ServiceBuilder sb = new ServiceBuilder()
                .provider( getProviderClass() )
                .apiKey( getApiKey() )
                .apiSecret( getApiSecret() );

        if ( !TextUtils.isEmpty( getScope() ) )
        {
            sb.scope( getScope() );
        }

        signingService = sb.build();
    }

    protected abstract String getScope();

    protected abstract String getApiSecret();

    protected abstract String getApiKey();

    protected abstract Class<? extends Api> getProviderClass();

    @Override
    public Response execute( Request request ) throws IOException
    {
        if ( !isUserLoggedIn() )
        {
            throw new IOException( "User not logged in" );
        }

        // First, sign the request
        OAuthRequest oAuthRequest = new OAuthRequest( getVerbForMethod( request.getMethod() ), request.getUrl() );
        signingService.signRequest( getAccessToken(), oAuthRequest );

        // Then switch the old headers with signed headers
        Map<String, String> signedHeadersMap = oAuthRequest.getHeaders();
        List<Header> signedHeaders = new ArrayList<Header>( signedHeadersMap.size() );
        for ( Map.Entry<String, String> header : signedHeadersMap.entrySet() )
        {
            signedHeaders.add( new Header( header.getKey(), header.getValue() ) );
        }

        // Switch old url with new url
        String signedUrl = oAuthRequest.getCompleteUrl();
        Helper.debug( signedUrl );

        // Make new request with signed headers and url
        Request signedRequest = new Request( request.getMethod(), signedUrl, signedHeaders, request.getBody() );

        // We need to check status of response in case it failed due to access token expiry
        Response response = super.execute( signedRequest );

        // 401 most likely indicates that access token has expired.
        // Time to refresh and resend the request
        if ( response.getStatus() == 401 && accessTokenCanExpire() )
        {
            // Do ensure that updateAccessToken() stores the value
            // of the updated access token in AccountPrefs.
            updateAccessToken();
            return execute( request );
        }
        return response;
    }

    protected abstract boolean accessTokenCanExpire();

    protected void updateAccessToken() throws IOException
    {

    }

    protected abstract Token getAccessToken();

    private Verb getVerbForMethod( String method )
    {
        for ( Verb verb : Verb.values() )
        {
            if ( method.equalsIgnoreCase( verb.name() ) )
            {
                return verb;
            }
        }
        throw new IllegalArgumentException( "Not a valid HTTP method : " + method );
    }

    protected abstract boolean isUserLoggedIn();
}
