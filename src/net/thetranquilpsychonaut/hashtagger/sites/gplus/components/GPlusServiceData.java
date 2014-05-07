package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.plus.model.Activity;

import java.util.List;
import java.util.Stack;

/**
 * Created by itwenty on 5/7/14.
 * Unlike Twitter4j and Facebook4j, Google's library does not implement serializable in any of it's classes,
 * making it impossible to pass them around in Intents and Bundles. So we use this class to hold such objects
 * statically.
 */
public class GPlusServiceData
{
    public static class AuthData
    {
        private static Stack<GoogleTokenResponse> authData = new Stack<GoogleTokenResponse>();

        public static void pushTokenResponse( GoogleTokenResponse tokenResponse )
        {
            if ( authData.size() != 0 )
            {
                throw new RuntimeException( "Auth Data is already set" );
            }
            authData.push( tokenResponse );
        }

        public static GoogleTokenResponse popTokenResponse()
        {
            if ( authData.size() <= 0 || authData.size() > 1 )
            {
                throw new RuntimeException( "Auth data either empty or has more than one element" );
            }
            return authData.pop();
        }
    }

    public static class SearchData
    {
        private static Stack<List<Activity>> searchData = new Stack<List<Activity>>();

        public static void pushSearchResults( List<Activity> results )
        {
            if ( searchData.size() != 0 )
            {
                throw new RuntimeException( "Search Data is already set" );
            }
            searchData.push( results );
        }

        public static List<Activity> popSearchResults()
        {
            if ( searchData.size() <= 0 || searchData.size() > 1 )
            {
                throw new RuntimeException( "Search data either empty or has more than one element" );
            }
            return searchData.pop();
        }
    }
}
