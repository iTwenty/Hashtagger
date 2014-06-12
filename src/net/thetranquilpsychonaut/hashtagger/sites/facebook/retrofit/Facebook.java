package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.SearchResult;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.QueryMap;

import java.util.Date;
import java.util.Map;

/**
 * Created by itwenty on 6/11/14.
 */
public class Facebook
{
    public static final String ENDPOINT = "https://graph.facebook.com/";
    private static volatile Api api;

    public static Api api()
    {

        if ( null == api )
        {
            synchronized ( Facebook.class )
            {
                if ( null == api )
                {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter( Date.class, new FacebookDateDeserializer() )
                            .create();

                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint( ENDPOINT )
                            .setClient( new FacebookSigningClient() )
                            .setLogLevel( RestAdapter.LogLevel.FULL )
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
        @GET("/v1.0/search?type=post")
        public SearchResult searchPosts( @QueryMap Map<String, String> params );
    }
}
