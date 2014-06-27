package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit;

import com.squareup.okhttp.OkHttpClient;
import net.thetranquilpsychonaut.hashtagger.config.InstagramConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSigningClient;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.components.InstagramApi;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.builder.api.Api;
import org.scribe.model.Token;

/**
 * Created by itwenty on 6/25/14.
 */
public class InstagramSigningClient extends SitesSigningClient
{
    public InstagramSigningClient( OkHttpClient client )
    {
        super( client );
    }

    @Override
    protected String getScope()
    {
        return InstagramConfig.INSTAGRAM_SCOPE;
    }

    @Override
    protected String getApiSecret()
    {
        return InstagramConfig.INSTAGRAM_CLIENT_SECRET;
    }

    @Override
    protected String getApiKey()
    {
        return InstagramConfig.INSTAGRAM_CLIENT_ID;
    }

    @Override
    protected Class<? extends Api> getProviderClass()
    {
        return InstagramApi.class;
    }

    @Override
    protected boolean accessTokenCanExpire()
    {
        return false;
    }

    @Override
    protected Token getAccessToken()
    {
        return new Token( AccountPrefs.getInstagramAccessToken(), "" );
    }

    @Override
    protected boolean isUserLoggedIn()
    {
        return AccountPrefs.areInstagramDetailsPresent();
    }
}
