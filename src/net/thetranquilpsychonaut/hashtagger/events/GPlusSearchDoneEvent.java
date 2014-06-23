package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.ActivityFeed;

/**
 * Created by itwenty on 6/23/14.
 */
public class GPlusSearchDoneEvent extends SitesSearchDoneEvent
{
    private ActivityFeed activityFeed;

    public GPlusSearchDoneEvent( int searchType, boolean success, ActivityFeed activityFeed )
    {
        super( searchType, success );
        this.activityFeed = activityFeed;
    }

    public ActivityFeed getActivityFeed()
    {
        return this.activityFeed;
    }
}
