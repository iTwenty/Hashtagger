package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Intent;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.FacebookConfig;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.ui.FacebookLoginActivity;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.io.Serializable;

/**
 * Created by itwenty on 4/7/14.
 */
public class FacebookService extends SitesService
{
    private volatile static boolean isServiceRunning;
    private static final String FACEBOOK_USERNAME_URL = "https://graph.facebook.com/me?fields=name";

    @Override
    protected Intent doSearch( Intent searchIntent )
    {
        final int searchType = searchIntent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
        final String hashtag = searchIntent.getStringExtra( HashtaggerApp.HASHTAG_KEY );
        Intent resultIntent = new Intent();
        resultIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        ResponseList<Post> responseList = null;
        try
        {
            if ( !AccountPrefs.areFacebookDetailsPresent() )
            {
                throw new FacebookException( "Facebook access token not found" );
            }
            Facebook facebook = new FacebookFactory( FacebookConfig.CONFIGURATION ).getInstance();
            facebook.setOAuthAccessToken( new AccessToken( AccountPrefs.getFacebookAccessToken() ) );
            switch ( searchType )
            {
                case SearchType.INITIAL:
                    responseList = facebook.searchPosts( hashtag );
                    break;
                case SearchType.OLDER:
                    responseList = facebook.fetchNext( FacebookSearchHandler.oldestPage );
                    break;
                case SearchType.NEWER:
                    responseList = facebook.fetchPrevious( FacebookSearchHandler.newestPage );
                    break;
                case SearchType.TIMED:
                    responseList = facebook.fetchPrevious( FacebookSearchHandler.newestPage );
                    break;
            }
        }
        catch ( FacebookException e )
        {
            Helper.debug( "Error while searching Facebook for " + hashtag + " : " + e.getMessage() );
        }
        int searchResult = null == responseList ? Result.FAILURE : Result.SUCCESS;
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
        return resultIntent;
    }

    @Override
    protected Intent doAuth( Intent intent )
    {
        final String code = intent.getStringExtra( FacebookLoginActivity.FACEBOOK_CODE_KEY );
        Intent resultIntent = new Intent();
        Token accessToken = null;
        String userName = null;
        try
        {
            OAuthService service = new ServiceBuilder()
                    .callback( FacebookLoginActivity.FACEBOOK_CALLBACK_URL )
                    .provider( FacebookApi.class )
                    .apiKey( FacebookConfig.FACEBOOK_OAUTH_APP_ID )
                    .apiSecret( FacebookConfig.FACEBOOK_OAUTH_APP_SECRET )
                    .build();
            accessToken = service.getAccessToken( null, new Verifier( code ) );
            OAuthRequest request = new OAuthRequest( Verb.GET, FACEBOOK_USERNAME_URL );
            service.signRequest( accessToken, request );
            Response response = request.send();
            userName = Helper.extractJsonStringField( response.getBody(), "name" );
        }
        catch ( Exception e )
        {
            Helper.debug( "Failed to get Facebook access token : " + e.getMessage() );
        }
        int accessResult = null == accessToken ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, accessResult );
        if ( accessResult == Result.SUCCESS )
        {
            resultIntent.putExtra( Result.RESULT_DATA, accessToken );
            resultIntent.putExtra( Result.RESULT_EXTRAS, userName );
        }
        return resultIntent;
    }

    @Override
    protected boolean isServiceRunning()
    {
        return isServiceRunning;
    }

    @Override
    protected void setServiceRunning( boolean running )
    {
        isServiceRunning = running;
    }

    public static void setIsServiceRunning( boolean running )
    {
        isServiceRunning = running;
    }

    public static boolean getIsServiceRunning()
    {
        return isServiceRunning;
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
