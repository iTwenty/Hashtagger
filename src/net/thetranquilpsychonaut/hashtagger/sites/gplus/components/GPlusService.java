package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import android.content.Intent;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.GPlusConfig;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by itwenty on 5/6/14.
 */
public class GPlusService extends SitesService
{
    private volatile static boolean isServiceRunning;

    @Override
    protected Intent doSearch( Intent searchIntent )
    {
        final int searchType = searchIntent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
        final String hashtag = searchIntent.getStringExtra( HashtaggerApp.HASHTAG_KEY );
        Intent resultIntent = new Intent();
        resultIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        ActivityFeed results = null;
        try
        {
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new AndroidJsonFactory();
            if ( !AccountPrefs.areGPlusDetailsPresent() )
            {
                throw new IOException( "Google+ access token not found" );
            }
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory( jsonFactory )
                    .setTransport( httpTransport )
                    .setClientSecrets( GPlusConfig.SECRETS )
                    .build();
            credential.setAccessToken( AccountPrefs.getGPlusAccessToken() );
            credential.setRefreshToken( AccountPrefs.getGPlusRefreshToken() );
            Plus plus = new Plus.Builder( httpTransport, jsonFactory, credential )
                    .setApplicationName( GPlusConfig.APP_NAME )
                    .build();
            Plus.Activities.Search searchActivities = plus.activities().search( hashtag );
            // Google+ API has no method to fetch results newer than specified token.
            // So we simply let it do a normal search for NEWER and TIMED searchtypes
            // and remove results we have already received in GPlusSearchHandler's onReceive()
            switch ( searchType )
            {
                case SearchType.INITIAL:
                    searchActivities.setMaxResults( HashtaggerApp.GPLUS_SEARCH_LIMIT );
                    break;
                case SearchType.OLDER:
                    searchActivities.setMaxResults( HashtaggerApp.GPLUS_SEARCH_LIMIT );
                    searchActivities.setPageToken( GPlusSearchHandler.nextPageToken );
                    break;
                default:
                    break;
            }
            results = searchActivities.execute();
        }
        catch ( IOException e )
        {
            Helper.debug( "Error while searching Google+ for " + hashtag + " : " + e.getMessage() );
        }
        int searchResult = null == results ? Result.FAILURE : Result.SUCCESS;
        if ( searchResult == Result.SUCCESS )
        {
            if ( searchType != SearchType.NEWER && searchType != SearchType.TIMED )
            {
                GPlusSearchHandler.nextPageToken = results.getNextPageToken();
            }
            GPlusData.SearchData.pushSearchResults( results.getItems() );
        }
        return resultIntent;
    }

    @Override
    protected Intent doAuth( Intent authIntent )
    {
        final String code = authIntent.getStringExtra( HashtaggerApp.GPLUS_CODE_KEY );
        Intent resultIntent = new Intent();
        GoogleTokenResponse tokenResponse = null;
        String userName = null;
        try
        {
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new AndroidJsonFactory();
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport,
                    jsonFactory,
                    GPlusConfig.SECRETS,
                    Arrays.asList( HashtaggerApp.GPLUS_ACCESS_SCOPE ) )
                    .build();
            tokenResponse = flow.newTokenRequest( code )
                    .setRedirectUri( HashtaggerApp.GPLUS_CALLBACK_URL )
                    .execute();
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory( jsonFactory )
                    .setTransport( httpTransport )
                    .setClientSecrets( GPlusConfig.SECRETS )
                    .build();
            credential.setAccessToken( tokenResponse.getAccessToken() );
            Person me = new Plus.Builder( httpTransport, jsonFactory, credential )
                    .setApplicationName( GPlusConfig.APP_NAME )
                    .build()
                    .people()
                    .get( "me" )
                    .execute();
            userName = me.getDisplayName();
        }
        catch ( IOException e )
        {
            Helper.debug( "Failed to get Google+ access token" + e.getMessage() );
        }
        int accessResult = null == tokenResponse ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, accessResult );
        if ( accessResult == Result.SUCCESS )
        {
            // As token response is not serializable, we use a static class to store it which our BroacastReceiver later reads from
            GPlusData.AuthData.pushTokenResponse( tokenResponse );
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
        return HashtaggerApp.GPLUS_LOGIN_ACTION;
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.GPLUS_SEARCH_ACTION;
    }
}
