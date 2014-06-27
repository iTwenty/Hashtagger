package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit;

import com.squareup.okhttp.OkHttpClient;
import net.thetranquilpsychonaut.hashtagger.config.TwitterConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSigningClient;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;

/**
 * Created by itwenty on 6/9/14.
 */
public class TwitterSigningClient extends SitesSigningClient
{
    public TwitterSigningClient( OkHttpClient client )
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
        return TwitterConfig.TWITTER_OAUTH_CONSUMER_SECRET;
    }

    @Override
    protected String getApiKey()
    {
        return TwitterConfig.TWITTER_OAUTH_CONSUMER_KEY;
    }

    @Override
    protected Class<? extends Api> getProviderClass()
    {
        return TwitterApi.class;
    }

    @Override
    protected boolean accessTokenCanExpire()
    {
        return false;
    }

    @Override
    protected Token getAccessToken()
    {
        return new Token( AccountPrefs.getTwitterAccessToken(), AccountPrefs.getTwitterAccessTokenSecret() );
    }

    @Override
    protected boolean isUserLoggedIn()
    {
        return AccountPrefs.areTwitterDetailsPresent();
    }
}
