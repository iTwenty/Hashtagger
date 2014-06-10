package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit;

import retrofit.RestAdapter;

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
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint( ENDPOINT )
                            .setClient( new GPlusSigningClient() )
                            .build();

                    api = adapter.create( Api.class );
                }
            }
        }
        return api;
    }

    public static interface Api
    {

    }
}
