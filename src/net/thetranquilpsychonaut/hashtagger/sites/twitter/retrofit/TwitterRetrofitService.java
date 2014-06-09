package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.SearchResult;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

import java.util.Date;
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
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter( Date.class, new TwitterStatusDateDeserializer() )
                            .create();

                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint( TwitterRetrofitService.ENDPOINT )
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
        @GET( "/1.1/search/tweets.json" )
        public SearchResult searchTweets( @QueryMap Map<String, String> params );

        @FormUrlEncoded
        @POST( "/1.1/statuses/update.json" )
        public void replyToStatus(
                @Field( "status" ) String statusText,
                @Field( "in_reply_to_status_id" ) String  inReplyToStatusId,
                Callback<Status> callback );

        @POST( "/1.1/statuses/retweet/{id}.json" )
        public void retweetStatus( @Path( "id" ) String statusId, Callback<Status> callback );

        @FormUrlEncoded
        @POST( "/1.1/favorites/create.json" )
        public void createFavorite( @Field( "id" ) String statusId,
                                    Callback<Status> callback );

        @FormUrlEncoded
        @POST( "/1.1/favorites/destroy.json" )
        public void destroyFavorite( @Field( "id" ) String statusId,
                                    Callback<Status> callback );
    }
}
