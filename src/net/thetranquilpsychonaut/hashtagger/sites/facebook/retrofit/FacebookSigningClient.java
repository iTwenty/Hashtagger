package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit;

import com.squareup.okhttp.OkHttpClient;
import net.thetranquilpsychonaut.hashtagger.config.FacebookConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSigningClient;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;

/**
 * Created by itwenty on 6/11/14.
 */
public class FacebookSigningClient extends SitesSigningClient
{
    public FacebookSigningClient( OkHttpClient client )
    {
        super( client );
    }

    @Override
    protected String getScope()
    {
        return null;
    }

    @Override
    protected String getApiSecret()
    {
        return FacebookConfig.FACEBOOK_OAUTH_APP_SECRET;
    }

    @Override
    protected String getApiKey()
    {
        return FacebookConfig.FACEBOOK_OAUTH_APP_ID;
    }

    @Override
    protected Class<? extends Api> getProviderClass()
    {
        return FacebookApi.class;
    }

    @Override
    protected boolean accessTokenCanExpire()
    {
        return false;
    }

    @Override
    protected Token getAccessToken()
    {
        return new Token( AccountPrefs.getFacebookAccessToken(), "" );
    }

    @Override
    protected boolean isUserLoggedIn()
    {
        return AccountPrefs.areFacebookDetailsPresent();
    }
}
