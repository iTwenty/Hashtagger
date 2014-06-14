package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.ActivityFeed;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.PeopleFeed;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

import java.util.Date;
import java.util.Map;

/**
 * Created by itwenty on 6/10/14.
 */
public class GPlus
{
    public static final String ENDPOINT = " https://www.googleapis.com/plus/v1/";
    private static volatile Api api;

    public static Api api()
    {
        if ( null == api )
        {
            synchronized ( GPlus.class )
            {
                if ( null == api )
                {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter( Date.class, new GPlusDateDeserializer() )
                            .create();

                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint( ENDPOINT )
                            .setClient( new GPlusSigningClient() )
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
        @GET("/activities")
        public ActivityFeed searchActivities( @QueryMap Map<String, String> params );

        @GET("/activities/{activityId}/people/{collection}")
        public void listByActivity(
                @Path("activityId") String activityId,
                @Path("collection") String collection,
                @QueryMap Map<String, String> listByActivityParams,
                Callback<PeopleFeed> callback );
    }
}
