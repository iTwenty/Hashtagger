package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ExpandableStatus;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.ArrayList;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterListAdapter extends SitesListAdapter
{
    Context ctx;

    public TwitterListAdapter( Context context, int textViewResourceId, ArrayList<ExpandableStatus> statuses )
    {
        super( context, textViewResourceId, statuses );
        ctx = context;
    }

    @Override
    protected SitesListRow initSitesListRow( Context context )
    {
        return new TwitterListRow( context );
    }
}
