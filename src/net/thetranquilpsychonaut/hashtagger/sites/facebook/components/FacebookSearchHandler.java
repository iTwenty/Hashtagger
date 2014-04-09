package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.FacebookConfig;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.SharedPreferencesHelper;

import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookSearchHandler extends SitesSearchHandler
{
    Facebook facebook;

    public FacebookSearchHandler( SitesSearchListener listener )
    {
        super( listener );
        this.facebook = new FacebookFactory( FacebookConfig.CONFIGURATION ).getInstance();
        // If user was previously logged in, we need to restore hir's credentials from shared prefs
        if ( SharedPreferencesHelper.areFacebookDetailsPresent() )
            setAccessToken();
    }

    public void setAccessToken()
    {
        if ( !SharedPreferencesHelper.areFacebookDetailsPresent() )
            throw new RuntimeException( "must be logged in before setting access token!" );
        facebook.setOAuthAccessToken( new AccessToken( SharedPreferencesHelper.getFacebookOauthAccessToken() ) );
    }

    public void clearAccessToken()
    {
        facebook.setOAuthAccessToken( null );
    }

    @Override
    protected Intent addExtraParameters( Intent searchIntent )
    {
        searchIntent.putExtra( HashtaggerApp.FACEBOOK_KEY, facebook );
        return searchIntent;
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return FacebookService.class;
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        SearchType searchType = ( SearchType ) intent.getSerializableExtra( SearchType.SEARCH_TYPE_KEY );
        Result resultType = ( Result ) intent.getSerializableExtra( Result.RESULT_KEY );
        if ( resultType == Result.FAILURE )
        {
            sitesSearchListener.onError( searchType );
            return;
        }
        List<Post> results = ( List<Post> ) intent.getSerializableExtra( Result.RESULT_DATA );
        Bundle outBundle = new Bundle();
        outBundle.putSerializable( HashtaggerApp.SEARCH_RESULT_LIST_KEY, ( java.io.Serializable ) results );
        sitesSearchListener.afterSearching( searchType, outBundle );
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.FACEBOOK_SEARCH_ACTION;
    }
}
