package net.thetranquilpsychonaut.hashtagger.sites.components;

import android.text.TextUtils;
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
        OAuthRequest oAuthRequest = new OAuthRequest( getVerbForMethod( request.getMethod() ), request.getUrl() );
        signingService.signRequest( getAccessToken(), oAuthRequest );
        Map<String, String> headers = oAuthRequest.getHeaders();
        List<Header> newHeaders = new ArrayList<Header>( headers.size() );
        for ( Map.Entry<String, String> header : headers.entrySet() )
        {
            newHeaders.add( new Header( header.getKey(), header.getValue() ) );
        }
        Request signedRequest = new Request( request.getMethod(), request.getUrl(), newHeaders, request.getBody() );
        return super.execute( signedRequest );
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
