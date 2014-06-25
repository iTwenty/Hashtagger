package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit;

import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.SearchResult;
import retrofit.RestAdapter;
import retrofit.http.GET;
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
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint( ENDPOINT )
                            .setClient( new InstagramSigningClient() )
                            .build();
                    api = adapter.create( Api.class );
                }
            }
        }
        return api;
    }

    public static interface Api
    {
        @GET( "/tags/{tagName}/media/recent" )
        public SearchResult getRecentMedia( @Path( "tagName" ) String tagName,
                                            @QueryMap Map<String, String> params );
    }
}
