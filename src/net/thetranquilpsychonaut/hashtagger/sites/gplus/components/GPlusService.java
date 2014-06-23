package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.GPlusConfig;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.events.GPlusAuthDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.GPlusSearchDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.GPlus;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.ActivityFeed;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.SearchParams;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.ui.GPlusLoginActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * Created by itwenty on 5/6/14.
 */
public class GPlusService extends SitesService
{
    private static String nextPageToken;

    private volatile static boolean isSearchRunning = false;

    private static final int    GPLUS_SEARCH_LIMIT = 20;
    private static final String GPLUS_USERNAME_URL = "https://www.googleapis.com/plus/v1/people/me?fields=displayName";

    @Override
    protected void doSearch( Intent searchIntent )
    {
        isSearchRunning = true;
        final int searchType = searchIntent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
        final String hashtag = searchIntent.getStringExtra( HashtaggerApp.HASHTAG_KEY );
        SearchParams params = new SearchParams( hashtag );
        ActivityFeed activityFeed = null;
        try
        {
            // Google+ API has no method to fetch results newer than specified activity.
            // So we simply let it do a normal search for NEWER and TIMED searchtypes
            // and remove results we have already received in GPlusSearchHandler's onReceive()
            switch ( searchType )
            {
                case SearchType.INITIAL:
                    params.setMaxResults( GPLUS_SEARCH_LIMIT );
                    break;
                case SearchType.OLDER:
                    params.setMaxResults( GPLUS_SEARCH_LIMIT );
                    params.setPageToken( nextPageToken );
                    break;
                default:
                    break;
            }
            activityFeed = GPlus.api().searchActivities( params.getParams() );
        }
        catch ( Exception e )
        {
            Helper.debug( "Error while searching Google+ for " + hashtag + " : " + e.getMessage() );
        }
        boolean success = null != activityFeed;
        if ( success )
        {
            if ( searchType != SearchType.NEWER && searchType != SearchType.TIMED )
            {
                nextPageToken = activityFeed.getNextPageToken();
            }
        }
        // Subscriber : GPlusSearchHandler : onGPlusSearchDone()
        HashtaggerApp.bus.post( new GPlusSearchDoneEvent( searchType, success, activityFeed ) );
        isSearchRunning = false;
    }

    @Override
    protected void doAuth( Intent authIntent )
    {
        final String code = authIntent.getStringExtra( SitesLoginHandler.VERIFIER_KEY );
        Token accessToken = null;
        String userName = null;
        try
        {
            OAuthService service = new ServiceBuilder()
                    .provider( Google2Api.class )
                    .callback( GPlusLoginActivity.GPLUS_CALLBACK_URL )
                    .apiKey( GPlusConfig.GPLUS_OAUTH_CLIENT_ID )
                    .apiSecret( GPlusConfig.GPLUS_OAUTH_CLIENT_SECRET )
                    .scope( GPlusConfig.GPLUS_ACCESS_SCOPE )
                    .build();
            accessToken = service.getAccessToken( null, new Verifier( code ) );
            OAuthRequest request = new OAuthRequest( Verb.GET, GPLUS_USERNAME_URL );
            service.signRequest( accessToken, request );
            Response response = request.send();
            userName = Helper.extractJsonStringField( response.getBody(), "displayName" );
        }
        catch ( Exception e )
        {
            Helper.debug( "Error while obtaining Google+ access token : " + e.getMessage() );
        }
        boolean success = null != accessToken;
        // Subscriber : GPlusLoginHandler : onGPlusAuthDone()
        HashtaggerApp.bus.post( new GPlusAuthDoneEvent( success, accessToken, userName ) );
    }

    public static boolean isSearchRunning()
    {
        return isSearchRunning;
    }
}
