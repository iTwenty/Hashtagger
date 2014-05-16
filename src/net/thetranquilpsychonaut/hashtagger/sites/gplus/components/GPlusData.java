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
public class GPlusData
{
    public static class AuthData
    {
        private static Stack<GoogleTokenResponse> authData = new Stack<GoogleTokenResponse>();

        public static void pushTokenResponse( GoogleTokenResponse tokenResponse )
        {
            authData.clear();
            authData.push( tokenResponse );
        }

        public static GoogleTokenResponse popTokenResponse()
        {
            if ( authData.isEmpty() )
            {
                throw new RuntimeException( "No GoogleTokenResponse to pop!" );
            }
            GoogleTokenResponse response = authData.pop();
            authData.clear();
            return response;
        }
    }

    public static class SearchData
    {
        private static Stack<List<Activity>> searchData = new Stack<List<Activity>>();

        public static void pushSearchResults( List<Activity> results )
        {
            searchData.clear();
            searchData.push( results );
        }

        public static List<Activity> popSearchResults()
        {
            if ( searchData.isEmpty() )
            {
                throw new RuntimeException( "No Activity list to pop!" );
            }
            List<Activity> activities = searchData.pop();
            searchData.clear();
            return activities;
        }
    }

    public static class ActivityData
    {
        private static Stack<Activity> activityData = new Stack<Activity>();

        public static void pushActivity( Activity activity )
        {
            activityData.clear();
            activityData.push( activity );
        }

        public static Activity popActivity()
        {
            if ( activityData.isEmpty() )
            {
                throw new RuntimeException( "No activity to pop!" );
            }
            Activity activity = activityData.pop();
            activityData.clear();
            return activity;
        }
    }
}
