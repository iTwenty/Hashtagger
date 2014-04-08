package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Intent;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.config.FacebookConfig;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;

/**
 * Created by itwenty on 4/7/14.
 */
public class FacebookService extends SitesService
{
    @Override
    protected Intent doSearch( Intent intent )
    {
        return null;
    }

    @Override
    protected Intent doAuth( Intent intent )
    {
        final String code = intent.getStringExtra( HashtaggerApp.FACEBOOK_CODE_KEY );
        Intent resultIntent = new Intent();
        AccessToken accessToken = null;
        String userName = null;
        try
        {
            Facebook facebook = new FacebookFactory( FacebookConfig.CONFIGURATION ).getInstance();
            facebook.setOAuthCallbackURL( HashtaggerApp.FACEBOOK_CALLBACK_URL );
            accessToken = facebook.getOAuthAccessToken( code );
            facebook.setOAuthAccessToken( accessToken );
            userName = facebook.users().getMe().getName();
        }
        catch ( FacebookException e )
        {
            Helper.debug( "Failed to get Facebook access token" );
        }
        Result accessResult = null == accessToken ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, accessResult );
        if ( accessResult == Result.SUCCESS )
        {
            resultIntent.putExtra( Result.RESULT_DATA, accessToken );
            resultIntent.putExtra( Result.RESULT_EXTRAS, userName );
        }
        return resultIntent;
    }

    @Override
    public String getLoginActionName()
    {
        return HashtaggerApp.FACEBOOK_LOGIN_ACTION;
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.FACEBOOK_SEARCH_ACTION;
    }
}
