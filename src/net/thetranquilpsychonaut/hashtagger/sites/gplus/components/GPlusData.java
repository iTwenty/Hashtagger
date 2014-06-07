package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import com.google.api.services.plus.model.Activity;

import java.util.Collections;
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
            List<Activity> activities = searchData.isEmpty() ? Collections.<Activity>emptyList() : searchData.pop();
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
            Activity activity = activityData.isEmpty() ? null : activityData.pop();
            activityData.clear();
            return activity;
        }
    }
}
