package net.thetranquilpsychonaut.hashtagger.sites.instagram.components;

import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.InstagramConfig;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.events.InstagramAuthDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.InstagramSearchDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.Instagram;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.SearchParams;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.SearchResult;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.ui.InstagramLoginActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramService extends SitesService
{
    private static final int     MAX_COUNT       = 20;
    private static       boolean isSearchRunning = false;

    private static String nextMaxId;
    private static String nextMinId;

    @Override
    protected void doSearch( Intent intent )
    {
        isSearchRunning = true;
        int searchType = intent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
        String hashtag = intent.getStringExtra( HashtaggerApp.HASHTAG_KEY );
        if ( hashtag.startsWith( "#" ) )
        {
            hashtag = hashtag.substring( 1, hashtag.length() );
        }
        hashtag = hashtag.replaceAll( "\\s+", "" );
        SearchParams params = new SearchParams();
        SearchResult searchResult = null;
        try
        {
            params.setCount( MAX_COUNT );
            switch ( searchType )
            {
                case SearchType.INITIAL:
                    break;
                case SearchType.OLDER:
                    params.setMaxTagId( nextMaxId );
                    break;
                case SearchType.NEWER:
                    params.setMinTagId( nextMinId );
                    break;
                case SearchType.TIMED:
                    params.setMinTagId( nextMinId );
                    break;
            }
            searchResult = Instagram.api().getRecentMedia( hashtag, params.getParams() );
        }
        catch ( Exception e )
        {
            Helper.debug( "Error while searching Instagram for " + hashtag + " : " + e.getMessage() );
        }
        boolean success = null != searchResult;
        if ( success )
        {
            if ( !Helper.isNullOrEmpty( searchResult.getData() ) )
            {
                if ( searchType != SearchType.OLDER )
                {
                    nextMinId = searchResult.getPagination().getNextMinId();
                }
                if ( searchType != SearchType.NEWER && searchType != SearchType.TIMED )
                {
                    nextMaxId = searchResult.getPagination().getNextMaxId();
                }
            }

        }
        // Subscriber : InstagramSearchHandler : onInstagramSearchDone()
        HashtaggerApp.bus.post( new InstagramSearchDoneEvent( searchType, success, searchResult ) );
        isSearchRunning = false;
    }

    @Override
    protected void doAuth( Intent intent )
    {
        final String code = intent.getStringExtra( SitesLoginHandler.VERIFIER_KEY );
        Token accessToken = null;
        String userName = null;
        try
        {
            OAuthService service = new ServiceBuilder()
                    .provider( InstagramApi.class )
                    .callback( InstagramLoginActivity.CALLBACK_URL )
                    .apiKey( InstagramConfig.INSTAGRAM_CLIENT_ID )
                    .apiSecret( InstagramConfig.INSTAGRAM_CLIENT_SECRET )
                    .scope( InstagramConfig.INSTAGRAM_SCOPE )
                    .build();
            accessToken = service.getAccessToken( null, new Verifier( code ) );
            userName = Helper.extractInstagramUserName( accessToken.getRawResponse() );
        }
        catch ( Exception e )
        {
            Helper.debug( "Error while obtaining Instagram access token : " + e.getMessage() );
        }
        boolean success = null != accessToken;
        // Subscriber : InstagramLoginHandler : onInstagramAuthDone()
        HashtaggerApp.bus.post( new InstagramAuthDoneEvent( success, accessToken, userName ) );
    }

    public static boolean isSearchRunning()
    {
        return isSearchRunning;
    }
}
