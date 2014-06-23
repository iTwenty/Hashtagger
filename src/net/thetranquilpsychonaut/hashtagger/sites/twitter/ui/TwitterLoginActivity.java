package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesLoginActivity;

/**
 * Created by itwenty on 3/17/14.
 */

public class TwitterLoginActivity extends SitesLoginActivity
{
    public static final String TWITTER_CALLBACK_URL       = "twitter-login-callback:///";
    public static final String TWITTER_OAUTH_VERIFIER_KEY = "oauth_verifier";

    @Override
    protected int getLoginTitleResId()
    {
        return R.string.str_title_activity_twitter_login;
    }

    @Override
    protected int getOAuthVersion()
    {
        return 1;
    }

    @Override
    protected String getAuthorizationUrl()
    {
        return null;
    }

    @Override
    protected SitesLoginHandler initSitesLoginHandler()
    {
        return new TwitterLoginHandler( this );
    }

    @Override
    protected String getVerifierKey()
    {
        return TWITTER_OAUTH_VERIFIER_KEY;
    }

    @Override
    protected String getCallbackUrl()
    {
        return TWITTER_CALLBACK_URL;
    }
}
