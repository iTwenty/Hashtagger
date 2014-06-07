package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit;

import net.thetranquilpsychonaut.hashtagger.config.TwitterConfig;
import net.thetranquilpsychonaut.hashtagger.sites.retrofit.SitesSigningClient;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * Created by itwenty on 6/7/14.
 */
public class TwitterSigningClient extends SitesSigningClient
{

    @Override
    protected Class<? extends Api> getProviderClass()
    {
        return TwitterApi.class;
    }

    @Override
    protected String getApiKey()
    {
        return TwitterConfig.TWITTER_OAUTH_CONSUMER_KEY;
    }

    @Override
    protected String getApiSecret()
    {
        return TwitterConfig.TWITTER_OAUTH_CONSUMER_SECRET;
    }

    @Override
    protected String getScope()
    {
        return null;
    }

    @Override
    protected boolean isAccessTokenPresent()
    {
        return AccountPrefs.areTwitterDetailsPresent();
    }

    @Override
    protected String getAccessToken()
    {
        return AccountPrefs.getTwitterAccessToken();
    }

    @Override
    protected String getAccessTokenSecret()
    {
        return AccountPrefs.getTwitterAccessTokenSecret();
    }
}
