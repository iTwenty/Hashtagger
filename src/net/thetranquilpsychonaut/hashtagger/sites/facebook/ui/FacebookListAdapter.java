package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookListAdapter extends SitesListAdapter
{
    Context ctx;

    public FacebookListAdapter( Context context, int textViewResourceId, List<?> posts )
    {
        super( context, textViewResourceId, posts );
        this.ctx = context;
    }

    @Override
    protected SitesListRow initSitesListRow( Context context )
    {
        return new FacebookListRow( context );
    }
}
