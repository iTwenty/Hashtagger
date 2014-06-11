package net.thetranquilpsychonaut.hashtagger.sites.ui;

import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

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
