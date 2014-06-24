package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.components.InstagramLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesLoginActivity;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramLoginActivity extends SitesLoginActivity
{
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
        // TODO
        return null;
    }

    @Override
    protected SitesLoginHandler initSitesLoginHandler()
    {
        return new InstagramLoginHandler( this );
    }

    @Override
    protected String getVerifierKey()
    {
        // TODO
        return null;
    }

    @Override
    protected String getCallbackUrl()
    {
        // TODO
        return null;
    }
}
