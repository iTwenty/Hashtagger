package net.thetranquilpsychonaut.hashtagger.sites.ui;

import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

import java.util.List;
import java.util.Stack;

/**
 * Created by itwenty on 5/28/14.
 */

// This class is used to hold data that can grow to large to be
// serialized/deserialized efficiently on config changes.
public final class PersistentData
{
    public static final class TwitterFragmentData
    {
        public static List<Status>  statuses;
        public static List<Integer> statusTypes;
    }

    public static final class GPlusFragmentData
    {
        public static List<Activity> activities;
        public static List<Integer>  activityTypes;
    }

    public static final class FacebookFragmentData
    {
        public static List<Post>    posts;
        public static List<Integer> postTypes;
    }

    public static final class SitesActivityData
    {
        public static Stack<String> hashtags;
    }
}
