package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit;

import com.squareup.okhttp.OkHttpClient;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Comments;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Likes;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.SearchResult;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

import java.util.Map;

/**
 * Created by itwenty on 6/24/14.
 */
public class Instagram
{
    public static final String ENDPOINT = "https://api.instagram.com/v1/";
    private static volatile Api api;

    public static Api api()
    {
        if ( null == api )
        {
            synchronized ( Instagram.class )
            {
                if ( null == api )
                {
                    OkHttpClient client = new OkHttpClient();
                    client.setCache( HashtaggerApp.cache );

                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint( ENDPOINT )
                            .setClient( new InstagramSigningClient( client ) )
                            .build();
                    api = adapter.create( Api.class );
                }
            }
        }
        return api;
    }

    public static interface Api
    {
        @GET("/tags/{tagName}/media/recent")
        public SearchResult getRecentMedia( @Path("tagName") String tagName,
                                            @QueryMap Map<String, String> params );

        @POST("/media/{mediaId}/likes")
        public void postLike( @Path("mediaId") String mediaId,
                              Callback<Response> callback );

        @DELETE("/media/{mediaId}/likes")
        public void deleteLike( @Path("mediaId") String mediaId,
                                Callback<Response> callback );

        @GET("/media/{mediaId}/likes")
        public void getLikes( @Path("mediaId") String mediaId,
                              Callback<Likes> callback );

        @GET("/media/{mediaId}/comments")
        public void getComments( @Path("mediaId") String mediaId,
                                 Callback<Comments> callback );
    }
}
