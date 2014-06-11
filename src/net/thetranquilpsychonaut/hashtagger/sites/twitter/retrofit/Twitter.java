package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.SearchResult;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.TrendLocation;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Trends;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by itwenty on 6/7/14.
 */
public class Twitter
{
    public static final     String ENDPOINT = "https://api.twitter.com/";
    private static volatile Api    api      = null;

    public static Api api()
    {
        if ( null == api )
        {
            synchronized ( Twitter.class )
            {
                if ( null == api )
                {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter( Date.class, new TwitterDateDeserializer() )
                            .create();

                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint( ENDPOINT )
                            .setClient( new TwitterSigningClient() )
                            .setConverter( new GsonConverter( gson ) )
                            .build();

                    api = adapter.create( Api.class );
                }
            }
        }
        return api;
    }

    public static interface Api
    {
        @GET("/1.1/search/tweets.json")
        public SearchResult searchTweets( @QueryMap Map<String, String> params );

        @POST("/1.1/statuses/update.json")
        public void replyToStatus(
                @Query("status") String statusText,
                @Query("in_reply_to_status_id") String inReplyToStatusId,
                Callback<Status> callback );

        @POST("/1.1/statuses/retweet/{id}.json")
        public void retweetStatus( @Path("id") String statusId, Callback<Status> callback );

        @POST("/1.1/favorites/create.json")
        public void createFavorite( @Query("id") String statusId,
                                    Callback<Status> callback );

        @POST("/1.1/favorites/destroy.json")
        public void destroyFavorite( @Query("id") String statusId,
                                     Callback<Status> callback );

        @GET("/1.1/trends/closest.json")
        public List<TrendLocation> getClosestTrendLocations(
                @Query("lat") double latitude,
                @Query("long") double longitude );

        @GET("/1.1/trends/available.json")
        public List<TrendLocation> getAvailableTrends();

        @GET("/1.1/trends/place.json")
        public List<Trends> getTrendsForPlace( @Query("id") int woeid );
    }
}
