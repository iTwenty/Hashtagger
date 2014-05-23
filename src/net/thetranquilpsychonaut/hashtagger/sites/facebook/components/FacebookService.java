package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Intent;
import facebook4j.*;
import facebook4j.auth.AccessToken;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.FacebookConfig;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.io.Serializable;

/**
 * Created by itwenty on 4/7/14.
 */
public class FacebookService extends SitesService
{
    private static boolean isSearchRunning;

    @Override
    protected Intent doSearch( Intent searchIntent )
    {
        isSearchRunning = true;
        final SearchType searchType = ( SearchType ) searchIntent.getSerializableExtra( SearchType.SEARCH_TYPE_KEY );
        final String hashtag = searchIntent.getStringExtra( HashtaggerApp.HASHTAG_KEY );
        Intent resultIntent = new Intent();
        resultIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        ResponseList<Post> responseList = null;
        try
        {
            if ( !AccountPrefs.areFacebookDetailsPresent() )
            {
                throw new FacebookException( "Facebook access token nor found" );
            }
            Facebook facebook = new FacebookFactory( FacebookConfig.CONFIGURATION ).getInstance();
            facebook.setOAuthAccessToken( new AccessToken( AccountPrefs.getFacebookAccessToken() ) );
            switch ( searchType )
            {
                case INITIAL:
                    responseList = facebook.searchPosts( hashtag );
                    break;
                case OLDER:
                    responseList = facebook.fetchNext( FacebookSearchHandler.oldestPage );
                    break;
                case NEWER:
                    responseList = facebook.fetchPrevious( FacebookSearchHandler.newestPage );
                    break;
                case TIMED:
                    responseList = facebook.fetchPrevious( FacebookSearchHandler.newestPage );
                    break;
            }
        }
        catch ( FacebookException e )
        {
            Helper.debug( "Error while searching Facebook for " + hashtag + " : " + e.getMessage() );
        }
        Result searchResult = null == responseList ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, searchResult );
        if ( searchResult == Result.SUCCESS )
        {
            if ( !responseList.isEmpty() )
            {
                if ( searchType != SearchType.OLDER )
                {
                    FacebookSearchHandler.newestPage = responseList.getPaging();
                }
                if ( searchType != SearchType.NEWER && searchType != SearchType.TIMED )
                {
                    FacebookSearchHandler.oldestPage = responseList.getPaging();
                }
            }
            resultIntent.putExtra( Result.RESULT_DATA, ( Serializable ) responseList );
        }
        isSearchRunning = true;
        return resultIntent;
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
            //Callback URL is needed for making a successful call.
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

    public static boolean isSearchRunning()
    {

        return isSearchRunning;
    }
}
