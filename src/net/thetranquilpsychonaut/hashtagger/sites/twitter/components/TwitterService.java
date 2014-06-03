package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.config.TwitterConfig;
import net.thetranquilpsychonaut.hashtagger.enums.AuthType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.List;

/**
 * Created by itwenty on 4/2/14.
 */
public class TwitterService extends SitesService
{
    private volatile static boolean isServiceRunning;

    @Override
    protected Intent doSearch( Intent searchIntent )
    {
        final int searchType = searchIntent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
        final String hashtag = searchIntent.getStringExtra( HashtaggerApp.HASHTAG_KEY );
        Intent resultIntent = new Intent();
        resultIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        Query query = new Query( hashtag );
        QueryResult queryResult = null;
        try
        {
            if ( !AccountPrefs.areTwitterDetailsPresent() )
            {
                throw new TwitterException( "Twitter access token not found" );
            }
            Twitter twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
            twitter.setOAuthAccessToken( new AccessToken( AccountPrefs.getTwitterAccessToken(), AccountPrefs.getTwitterAccessTokenSecret() ) );

             /*
            for our initial search we dont need either max or since id.
            Older search retrieves tweets with ids lower than the maxId
            Newer search retrieves tweets with ids higher than the sinceId
             */

            switch ( searchType )
            {
                case SearchType.INITIAL:
                    query.setCount( HashtaggerApp.TWITTER_SEARCH_LIMIT );
                    break;
                case SearchType.OLDER:
                    query.setCount( HashtaggerApp.TWITTER_SEARCH_LIMIT );
                    query.setMaxId( TwitterSearchHandler.maxId );
                    break;
                case SearchType.NEWER:
                    query.setSinceId( TwitterSearchHandler.sinceId );
                    break;
                case SearchType.TIMED:
                    query.setSinceId( TwitterSearchHandler.sinceId );
            }
            queryResult = twitter.search( query );
        }
        catch ( TwitterException e )
        {
            Helper.debug( "Error while searching Twitter for " + hashtag + " : " + e.getMessage() );
        }
        int searchResult = null == queryResult ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, searchResult );
        if ( searchResult == Result.SUCCESS )
        {
            if ( !queryResult.getTweets().isEmpty() )
            {
                /*
                if our current search is the initial one, we set both the max and since ids for subsquent searches.
                if our current search is older, we don't want it to change the sinceId for our next newer search.
                if our current search is newer, we don't want it to change the maxId for our next older search.
                 */
                if ( searchType != SearchType.OLDER )
                {
                    TwitterSearchHandler.sinceId = queryResult.getMaxId();
                }
                if ( searchType != SearchType.NEWER && searchType != SearchType.TIMED )
                {
                    TwitterSearchHandler.maxId = queryResult.getSinceId() == 0 ? getLowestId( queryResult.getTweets() ) : queryResult.getSinceId();
                }
                // In case the search was for older results, we remove the newest one as maxId parameter is inclusive
                // and causes tweet to repeat.
                if ( searchType == SearchType.OLDER )
                {
                    queryResult.getTweets().remove( 0 );
                }
            }
            resultIntent.putExtra( Result.RESULT_DATA, queryResult );
        }
        return resultIntent;
    }

    private long getLowestId( List<Status> list )
    {
        return list.get( list.size() - 1 ).getId();
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
                final RequestToken requestToken = ( RequestToken ) intent.getSerializableExtra( HashtaggerApp.TWITTER_REQUEST_TOKEN_KEY );
                final String oauthVerifier = intent.getStringExtra( HashtaggerApp.TWITTER_OAUTH_VERIFIER_KEY );
                resultIntent = doAccessAuth( resultIntent, requestToken, oauthVerifier );
                break;
            default:
                throw new RuntimeException( "Invalid authorization type" );
        }
        return resultIntent;
    }

    private Intent doRequestAuth( Intent resultIntent )
    {
        RequestToken requestToken = null;
        try
        {
            Twitter twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
            requestToken = twitter.getOAuthRequestToken( HashtaggerApp.TWITTER_CALLBACK_URL );
        }
        catch ( TwitterException e )
        {
            Helper.debug( "Error while obtaining twitter request token : " + e.getMessage() );
        }
        int requestResult = null == requestToken ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, requestResult );
        if ( requestResult == Result.SUCCESS )
        {
            resultIntent.putExtra( Result.RESULT_DATA, requestToken );
        }
        return resultIntent;
    }

    private Intent doAccessAuth( Intent resultIntent, RequestToken requestToken, String oauthVerifier )
    {
        AccessToken accessToken = null;
        String userName = null;
        try
        {
            Twitter twitter = new TwitterFactory( TwitterConfig.CONFIGURATION ).getInstance();
            accessToken = twitter.getOAuthAccessToken( requestToken, oauthVerifier );
            twitter.setOAuthAccessToken( accessToken );
            userName = twitter.verifyCredentials().getName();
        }
        catch ( TwitterException e )
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
