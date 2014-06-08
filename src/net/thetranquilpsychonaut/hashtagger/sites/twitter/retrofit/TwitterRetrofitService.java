package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit;

import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.SearchResult;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Tweet;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

import java.util.Map;

/**
 * Created by itwenty on 6/7/14.
 */
public interface TwitterRetrofitService
{
    @GET( "/1.1/search/tweets.json" )
    SearchResult searchTweets( @QueryMap Map<String, String> params );
}
