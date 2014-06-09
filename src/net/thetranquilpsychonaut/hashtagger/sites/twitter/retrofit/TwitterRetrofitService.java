package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit;

import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.SearchResult;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

import java.util.Map;

/**
 * Created by itwenty on 6/7/14.
 */
public class TwitterRetrofitService
{
    public static final     String ENDPOINT = "https://api.twitter.com/";
    private static volatile Api    api      = null;

    public static Api api()
    {
        if ( null == api )
        {
            synchronized ( TwitterRetrofitService.class )
            {
                if ( null == api )
                {
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint( TwitterRetrofitService.ENDPOINT )
                            .setClient( new TwitterSigningClient() )
                            .build();
                    api = adapter.create( Api.class );
                }
            }
        }
        return api;
    }

    public static interface Api
    {
        @GET( "/1.1/search/tweets.json" )
        public void searchTweets( @QueryMap Map<String, String> params, Callback<SearchResult> result );
    }
}
