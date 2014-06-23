package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import net.thetranquilpsychonaut.hashtagger.config.FacebookConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.components.FacebookLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesLoginActivity;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookLoginActivity extends SitesLoginActivity
{
    public static final String FACEBOOK_CALLBACK_URL  = "http://localhost/";
    public static final String FACEBOOK_CODE_KEY      = "code";
    public static final String FACEBOOK_AUTHORIZE_URL = String.format( "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s", FacebookConfig.FACEBOOK_OAUTH_APP_ID, FACEBOOK_CALLBACK_URL );

    @Override
    protected int getOAuthVersion()
    {
        return 2;
    }

    @Override
    protected String getAuthorizationUrl()
    {
        return FACEBOOK_AUTHORIZE_URL;
    }

    @Override
    protected SitesLoginHandler initSitesLoginHandler()
    {
        return new FacebookLoginHandler( this );
    }

    @Override
    protected String getVerifierKey()
    {
        return FACEBOOK_CODE_KEY;
    }

    @Override
    protected String getCallbackUrl()
    {
        return FACEBOOK_CALLBACK_URL;
    }
}
