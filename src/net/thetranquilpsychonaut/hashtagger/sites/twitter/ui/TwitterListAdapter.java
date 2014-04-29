package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.List;

/**
 * Created by itwenty on 2/26/14.
 */
public class TwitterListAdapter extends SitesListAdapter
{
    public static final int STATUS_TYPE_NORMAL = 1;
    public static final int STATUS_TYPE_MEDIA  = 2;
    public static final int STATUS_TYPE_LINK   = 3;

    public TwitterListAdapter( Context context, int textViewResourceId, List<?> results )
    {
        super( context, textViewResourceId, results );
    }

    @Override
    protected SitesListRow initSitesListRow( Context context )
    {
        return new TwitterListRow( getContext() );
    }
}
