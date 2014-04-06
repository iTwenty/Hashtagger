package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Intent;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.enums.AuthType;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesService;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.List;

/**
 * Created by itwenty on 4/2/14.
 */
public class TwitterService extends SitesService
{
    @Override
    protected Intent doSearch( Intent searchIntent )
    {
        final SearchType searchType = ( SearchType ) searchIntent.getSerializableExtra( SearchType.SEARCH_TYPE_KEY );
        final Twitter twitter = ( Twitter ) searchIntent.getSerializableExtra( HashtaggerApp.TWITTER_KEY );
        final String hashtag = searchIntent.getStringExtra( HashtaggerApp.HASHTAG_KEY );
        Intent resultIntent = new Intent();
        resultIntent.putExtra( SearchType.SEARCH_TYPE_KEY, searchType );
        Query query = new Query( hashtag );
        QueryResult queryResult = null;
        query.setCount( HashtaggerApp.TWITTER_SEARCH_LIMIT );
        try
        {
            if ( null == twitter.getOAuthAccessToken() )
                throw new TwitterException( "" );
             /*
            for our initial search we dont need either max or since id.
            Older search retrieves tweets with ids lower than the maxId
            Newer search retrieves tweets with ids higher than the sinceId
             */
            switch ( searchType )
            {
                case INITIAL:
                    break;
                case OLDER:
                    query.setMaxId( TwitterSearchHandler.maxId );
                    break;
                case NEWER:
                    query.setSinceId( TwitterSearchHandler.sinceId );
                    break;
            }
            queryResult = twitter.search( query );
        }
        catch ( TwitterException e )
        {
            Helper.debug( "Error while searching for " + hashtag );
        }
        Result searchResult = null == queryResult ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, searchResult );
        if ( searchResult == Result.SUCCESS )
        {
            if ( !queryResult.getTweets().isEmpty() )
            {
                // In case the search was for older results, we remove the newest one as maxId parameter is inclusive
                // and causes tweet to repeat.
                if ( searchType == SearchType.OLDER )
                    queryResult.getTweets().remove( 0 );
                    /*
                    if our current search is the initial one, we set both the max and since ids for subsquent searches.
                    if our current search is older, we don't want it to change the sinceId for our next newer search.
                    if our current search is newer, we don't want it to change the maxId for our next older search.
                     */
                if ( searchType != SearchType.OLDER )
                    TwitterSearchHandler.sinceId = queryResult.getMaxId();
                if ( searchType != SearchType.NEWER )
                    TwitterSearchHandler.maxId = queryResult.getSinceId() == 0 ? getLowestId( queryResult.getTweets() ) : queryResult.getSinceId();
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

        final Twitter twitter = ( Twitter ) intent.getSerializableExtra( HashtaggerApp.TWITTER_KEY );
        final AuthType authType = ( AuthType ) intent.getSerializableExtra( AuthType.AUTH_TYPE_KEY );
        Intent resultIntent = new Intent();
        resultIntent.putExtra( AuthType.AUTH_TYPE_KEY, authType );
        switch ( authType )
        {
            case REQUEST:
                resultIntent = doRequestAuth( resultIntent, twitter );
                break;
            case ACCESS:
                final RequestToken requestToken = ( RequestToken ) intent.getSerializableExtra( HashtaggerApp.TWITTER_REQUEST_TOKEN_KEY );
                final String oauthVerifier = intent.getStringExtra( HashtaggerApp.OAUTH_VERIFIER_KEY );
                resultIntent = doAccessAuth( resultIntent, twitter, requestToken, oauthVerifier );
                break;
            default:
                throw new RuntimeException( "Invalid authorization type" );
        }
        return resultIntent;
    }

    private Intent doRequestAuth( Intent resultIntent, Twitter twitter )
    {
        RequestToken requestToken = null;
        try
        {
            requestToken = twitter.getOAuthRequestToken( HashtaggerApp.CALLBACK_URL );
        }
        catch ( TwitterException e )
        {
            Helper.debug( "Error while obtaining request token" );
        }
        Result authResult = null == requestToken ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, authResult );
        if ( authResult == Result.SUCCESS )
        {
            resultIntent.putExtra( Result.RESULT_DATA, requestToken );
        }
        return resultIntent;
    }

    private Intent doAccessAuth( Intent resultIntent, Twitter twitter, RequestToken requestToken, String oauthVerifier )
    {
        AccessToken accessToken = null;
        String userName = null;
        try
        {
            accessToken = twitter.getOAuthAccessToken( requestToken, oauthVerifier );
            twitter.setOAuthAccessToken( accessToken );
            userName = twitter.verifyCredentials().getName();
        }
        catch ( TwitterException e )
        {
            Helper.debug( "Error while obtaining access token" );
        }
        Result authResult = null == accessToken ? Result.FAILURE : Result.SUCCESS;
        resultIntent.putExtra( Result.RESULT_KEY, authResult );
        if ( authResult == Result.SUCCESS )
        {
            resultIntent.putExtra( Result.RESULT_DATA, accessToken );
            resultIntent.putExtra( Result.RESULT_EXTRAS, userName );
        }
        return resultIntent;
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
