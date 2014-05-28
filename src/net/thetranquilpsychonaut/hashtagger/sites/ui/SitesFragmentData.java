package net.thetranquilpsychonaut.hashtagger.sites.ui;

import com.google.api.services.plus.model.Activity;
import facebook4j.Post;
import twitter4j.Status;

import java.util.List;

/**
 * Created by itwenty on 5/28/14.
 */
public final class SitesFragmentData
{
    public static final class Twitter
    {
        public static List<Status>  statuses;
        public static List<Integer> statusTypes;
    }

    public static final class Facebook
    {
        public static List<Post>    posts;
        public static List<Integer> postTypes;
    }

    public static final class GPlus
    {
        public static List<Activity> activities;
        public static List<Integer>  activityTypes;
    }
}
