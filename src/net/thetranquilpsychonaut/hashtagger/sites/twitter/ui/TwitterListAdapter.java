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
