package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by itwenty on 6/7/14.
 */
public interface TwitterRetrofitService
{
    @GET( "/1.1/search/tweets.json" )
    void searchTweets( @Query( "q" ) String query, Callback<Response> results );
}
