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
import com.google.api.services.plus.model.Person;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.config.GPlusConfig;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by itwenty on 5/6/14.
 */
public class GPlusService extends SitesService
{
    @Override
    protected Intent doSearch( Intent intent )
    {
        return null;
    }

    @Override
    protected Intent doAuth( Intent intent )
    {
        final String code = intent.getStringExtra( HashtaggerApp.GPLUS_CODE_KEY );
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
            resultIntent.putExtra( Result.RESULT_DATA, new GPlusSerializableTokenResponse( tokenResponse ) );
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
