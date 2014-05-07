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
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Person;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.SharedPreferencesHelper;
import net.thetranquilpsychonaut.hashtagger.config.GPlusConfig;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by itwenty on 5/6/14.
 */
public class GPlusService extends SitesService
{
    @Override
    protected Intent doSearch( Intent searchIntent )
    {
        final SearchType searchType = ( SearchType ) searchIntent.getSerializableExtra( SearchType.SEARCH_TYPE_KEY );
        final String hashtag = searchIntent.getStringExtra( HashtaggerApp.HASHTAG_KEY );
        Intent resultIntent = new Intent();
        resultIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        List<Activity> results = null;
        try
        {
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new AndroidJsonFactory();
            if ( !SharedPreferencesHelper.areGPlusDetailsPresent() )
            {
                throw new IOException( "Google+ access token not found" );
            }
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory( jsonFactory )
                    .setTransport( httpTransport )
                    .setClientSecrets( GPlusConfig.SECRETS )
                    .build();
            credential.setAccessToken( SharedPreferencesHelper.getGPlusAccessToken() );
            credential.setRefreshToken( SharedPreferencesHelper.getGPlusRefreshToken() );
            Plus plus = new Plus.Builder( httpTransport, jsonFactory, credential )
                    .build();
            Plus.Activities.Search searchActivities = plus.activities().search( hashtag );
            searchActivities.setMaxResults( 20L );
            results = searchActivities.execute().getItems();
        }
        catch ( IOException e )
        {
            Helper.debug( "Error while searching Google+ for " + hashtag + " : " + e.getMessage() );
        }
        Result searchResult = null == results ? Result.FAILURE : Result.SUCCESS;
        if ( searchResult == Result.SUCCESS )
        {
            GPlusServiceData.SearchData.pushSearchResults( results );
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
            Person me = new Plus.Builder( httpTransport, jsonFactory, credential ).build().people().get( "me" ).execute();
            userName = me.getDisplayName();
        }
        catch ( IOException e )
        {
            Helper.debug( "Failed to get Google+ access token" );
        }
        Result accessResult = null == tokenResponse ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, accessResult );
        if ( accessResult == Result.SUCCESS )
        {
            // As token response is not serializable, we use a static class to store it which our BroacastReceiver later reads from
            GPlusServiceData.AuthData.pushTokenResponse( tokenResponse );
            resultIntent.putExtra( Result.RESULT_EXTRAS, userName );
        }
        return resultIntent;
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
