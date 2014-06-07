package net.thetranquilpsychonaut.hashtagger.sites.retrofit;

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
 * Created by itwenty on 6/7/14.
 */
public abstract class SitesSigningClient extends OkClient
{
    OAuthService signingService;

    protected SitesSigningClient()
    {
        ServiceBuilder sb = new ServiceBuilder();
        sb.provider( getProviderClass() );
        sb.apiKey( getApiKey() );
        sb.apiSecret( getApiSecret() );
        if ( !TextUtils.isEmpty( getScope() ) )
        {
            sb.scope( getScope() );
        }
        signingService = sb.build();
    }

    protected abstract Class<? extends Api> getProviderClass();

    protected abstract String getApiKey();

    protected abstract String getApiSecret();

    protected abstract String getScope();

    protected abstract boolean isAccessTokenPresent();

    protected abstract String getAccessToken();

    protected abstract String getAccessTokenSecret();

    @Override
    public Response execute( Request request ) throws IOException
    {
        OAuthRequest oAuthRequest = new OAuthRequest( getVerbForMethod( request.getMethod() ), request.getUrl() );
        if ( !isAccessTokenPresent() )
        {
            throw new IOException( "Access Token not found for " + getProviderClass().getCanonicalName() );
        }
        Token token = new Token( getAccessToken(), getAccessTokenSecret() );
        signingService.signRequest( token, oAuthRequest );
        List<Header> headersList = new ArrayList<Header>( oAuthRequest.getHeaders().size() );
        for ( Map.Entry<String, String> header : oAuthRequest.getHeaders().entrySet() )
        {
            headersList.add( new Header( header.getKey(), header.getValue() ) );
        }
        Request signedRequest = new Request( request.getMethod(), request.getUrl(), headersList, request.getBody() );
        return super.execute( signedRequest );
    }

    private Verb getVerbForMethod( String method )
    {
        if ( method.equalsIgnoreCase( "GET" ) )
        {
            return Verb.GET;
        }
        if ( method.equalsIgnoreCase( "POST" ) )
        {
            return Verb.POST;
        }
        if ( method.equalsIgnoreCase( "PUT" ) )
        {
            return Verb.PUT;
        }
        if ( method.equalsIgnoreCase( "DELETE" ) )
        {
            return Verb.DELETE;
        }
        if ( method.equalsIgnoreCase( "HEAD" ) )
        {
            return Verb.HEAD;
        }
        if ( method.equalsIgnoreCase( "OPTIONS" ) )
        {
            return Verb.OPTIONS;
        }
        if ( method.equalsIgnoreCase( "TRACE" ) )
        {
            return Verb.TRACE;
        }
        if ( method.equalsIgnoreCase( "PATCH" ) )
        {
            return Verb.PATCH;
        }
        throw new IllegalArgumentException( "Not a valid method : " + method );
    }
}
