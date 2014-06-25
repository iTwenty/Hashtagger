package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.config.InstagramConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.components.InstagramLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesLoginActivity;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramLoginActivity extends SitesLoginActivity
{
    public static final String CALLBACK_URL       = "http://localhost/";
    public static final String INSTAGRAM_CODE_KEY = "code";
    public static final String AUTHORIZATION_URL  =
            String.format( "https://api.instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=code&scope=%s",
                    InstagramConfig.INSTAGRAM_CLIENT_ID, CALLBACK_URL, InstagramConfig.INSTAGRAM_SCOPE );

    @Override
    protected int getLoginTitleResId()
    {
        return R.string.str_title_activity_instagram_login;
    }

    @Override
    protected int getOAuthVersion()
    {
        return 2;
    }

    @Override
    protected String getAuthorizationUrl()
    {
        return AUTHORIZATION_URL;
    }

    @Override
    protected SitesLoginHandler initSitesLoginHandler()
    {
        return new InstagramLoginHandler( this );
    }

    @Override
    protected String getVerifierKey()
    {
        return INSTAGRAM_CODE_KEY;
    }

    @Override
    protected String getCallbackUrl()
    {
        return CALLBACK_URL;
    }
}
