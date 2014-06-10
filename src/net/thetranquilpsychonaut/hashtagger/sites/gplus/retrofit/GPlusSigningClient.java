package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit;

import net.thetranquilpsychonaut.hashtagger.config.GPlusConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSigningClient;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.Google2Api;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.builder.api.Api;
import org.scribe.model.Token;

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
    protected Token getAccessToken()
    {
        return new Token( null, AccountPrefs.getGPlusAccessToken() );
    }

    @Override
    protected boolean isUserLoggedIn()
    {
        return AccountPrefs.areGPlusDetailsPresent();
    }
}
