package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookListAdapter extends SitesListAdapter
{
    public static final int POST_TYPE_OBJECT  = 1;
    public static final int POST_TYPE_DETAILS = 2;
    public static final int POST_TYPE_NORMAL  = 3;

    public FacebookListAdapter( Context context, int textViewResourceId, List<?> posts )
    {
        super( context, textViewResourceId, posts );
    }

    @Override
    protected SitesListRow initSitesListRow( Context context )
    {
        return new FacebookListRow( context );
    }
}
