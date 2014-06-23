package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import net.thetranquilpsychonaut.hashtagger.config.GPlusConfig;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesLoginActivity;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusLoginActivity extends SitesLoginActivity
{
    public static final  String GPLUS_CALLBACK_URL  = "http://localhost/";
    public static final  String GPLUS_CODE_KEY      = "code";
    private static final String GPLUS_AUTHORIZE_URL =
            String.format( "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s&scope=%s",
                    GPlusConfig.GPLUS_OAUTH_CLIENT_ID, GPLUS_CALLBACK_URL, GPlusConfig.GPLUS_ACCESS_SCOPE );

    @Override
    protected int getOAuthVersion()
    {
        return 2;
    }

    @Override
    protected String getAuthorizationUrl()
    {
        return GPLUS_AUTHORIZE_URL;
    }

    @Override
    protected SitesLoginHandler initSitesLoginHandler()
    {
        return new GPlusLoginHandler( this );
    }

    @Override
    protected String getVerifierKey()
    {
        return GPLUS_CODE_KEY;
    }

    @Override
    protected String getCallbackUrl()
    {
        return GPLUS_CALLBACK_URL;
    }
}
