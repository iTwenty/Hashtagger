package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Intent;
import android.text.TextUtils;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.TwitterConfig;
import net.thetranquilpsychonaut.hashtagger.enums.AuthType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.Twitter;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.SearchParams;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.SearchResult;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterLoginActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.List;

/**
 * Created by itwenty on 4/2/14.
 */
public class TwitterService extends SitesService
{
    /*
    max and since ids are used to navigate through the tweets timeline.
    tweet ids are time based i.e later tweets have higher ids than older tweets.
    */

    private static String maxId;
    private static String sinceId;

    private volatile static boolean isServiceRunning;
    private static final String TWITTER_USERNAME_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final int    TWITTER_SEARCH_LIMIT = 20;

    @Override
    protected Intent doSearch( Intent searchIntent )
    {
        final int searchType = searchIntent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
        final String hashtag = searchIntent.getStringExtra( HashtaggerApp.HASHTAG_KEY );
        Intent resultIntent = new Intent();
        resultIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        SearchParams params = new SearchParams( hashtag );
        SearchResult searchResult = null;
        try
        {
            // for our initial search we dont need either max or since myId.
            // Older search retrieves tweets with ids lower than the maxId
            // Newer search retrieves tweets with ids higher than the sinceId

            switch ( searchType )
            {
                case SearchType.INITIAL:
                    params.setCount( TWITTER_SEARCH_LIMIT );
                    break;
                case SearchType.OLDER:
                    params.setCount( TWITTER_SEARCH_LIMIT );
                    params.setMaxId( maxId );
                    break;
                case SearchType.NEWER:
                    params.setSinceId( sinceId );
                    break;
                case SearchType.TIMED:
                    params.setSinceId( sinceId );
            }
            searchResult = Twitter.api().searchTweets( params.getParams() );
        }
        catch ( Exception e )
        {
            Helper.debug( "Error while searching Twitter for " + hashtag + " : " + e.getMessage() );
        }
        int result = null == searchResult ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, result );
        if ( result == Result.SUCCESS )
        {
            if ( !searchResult.getStatuses().isEmpty() )
            {
                //  if our current search is the initial one,
                //  we set both the max and since ids for subsquent searches.
                //  if our current search is older,
                //  we don't want it to change the sinceId for our next newer search.
                //  if our current search is newer,
                //  we don't want it to change the maxId for our next older search.

                if ( searchType != SearchType.OLDER )
                {
                    sinceId = searchResult.getSearchMetadata().getMaxIdStr();
                }
                if ( searchType != SearchType.NEWER && searchType != SearchType.TIMED )
                {
                    maxId = TextUtils.equals( searchResult.getSearchMetadata().getSinceIdStr(), "0" ) ?
                            getLowestId( searchResult.getStatuses() ) :
                            searchResult.getSearchMetadata().getSinceIdStr();
                }
                // In case the search was for older results,
                // we remove the newest one as maxId parameter is inclusive
                // and causes tweet to repeat.
                if ( searchType == SearchType.OLDER )
                {
                    searchResult.getStatuses().remove( 0 );
                }
            }
            resultIntent.putExtra( Result.RESULT_DATA, searchResult );
        }
        return resultIntent;
    }

    private String getLowestId( List<Status> list )
    {
        return list.get( list.size() - 1 ).getIdStr();
    }

    @Override
    protected Intent doAuth( Intent intent )
    {
        final int authType = intent.getIntExtra( AuthType.AUTH_TYPE_KEY, -1 );
        Intent resultIntent = new Intent();
        resultIntent.putExtra( AuthType.AUTH_TYPE_KEY, authType );
        switch ( authType )
        {
            case AuthType.REQUEST:
                resultIntent = doRequestAuth( resultIntent );
                break;
            case AuthType.ACCESS:
                final Token requestToken = ( Token ) intent.getSerializableExtra( TwitterLoginActivity.TWITTER_REQUEST_TOKEN_KEY );
                final String oauthVerifier = intent.getStringExtra( TwitterLoginActivity.TWITTER_OAUTH_VERIFIER_KEY );
                resultIntent = doAccessAuth( resultIntent, requestToken, oauthVerifier );
                break;
            default:
                resultIntent.putExtra( Result.RESULT_KEY, Result.FAILURE );
        }
        return resultIntent;
    }

    private Intent doRequestAuth( Intent resultIntent )
    {
        Token token = null;
        String authorizationUrl = null;
        try
        {
            OAuthService service = new ServiceBuilder()
                    .provider( TwitterApi.SSL.class )
                    .callback( TwitterLoginActivity.TWITTER_CALLBACK_URL )
                    .apiKey( TwitterConfig.TWITTER_OAUTH_CONSUMER_KEY )
                    .apiSecret( TwitterConfig.TWITTER_OAUTH_CONSUMER_SECRET )
                    .build();
            token = service.getRequestToken();
            authorizationUrl = service.getAuthorizationUrl( token );
        }
        catch ( OAuthConnectionException e )
        {
            Helper.debug( "Error while obtaining twitter request token : " + e.getMessage() );
        }
        int requestResult = null == token ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, requestResult );
        if ( requestResult == Result.SUCCESS )
        {
            resultIntent.putExtra( Result.RESULT_DATA, token );
            resultIntent.putExtra( Result.RESULT_EXTRAS, authorizationUrl );
        }
        return resultIntent;
    }

    private Intent doAccessAuth( Intent resultIntent, Token requestToken, String oauthVerifier )
    {
        Token accessToken = null;
        String userName = null;
        try
        {
            OAuthService service = new ServiceBuilder()
                    .provider( TwitterApi.SSL.class )
                    .callback( TwitterLoginActivity.TWITTER_CALLBACK_URL )
                    .apiKey( TwitterConfig.TWITTER_OAUTH_CONSUMER_KEY )
                    .apiSecret( TwitterConfig.TWITTER_OAUTH_CONSUMER_SECRET )
                    .build();
            accessToken = service.getAccessToken( requestToken, new Verifier( oauthVerifier ) );

            // Once we have the access token, we do a quick and dirty request to get the username
            OAuthRequest request = new OAuthRequest( Verb.GET, TWITTER_USERNAME_URL );
            service.signRequest( accessToken, request );
            Response response = request.send();
            userName = Helper.extractJsonStringField( response.getBody(), "name" );
        }
        catch ( Exception e )
        {
            Helper.debug( "Error while obtaining twitter access token : " + e.getMessage() );
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
    public String getSearchActionName()
    {
        return HashtaggerApp.TWITTER_SEARCH_ACTION;
    }

    @Override
    public String getLoginActionName()
    {
        return HashtaggerApp.TWITTER_LOGIN_ACTION;
    }
}
