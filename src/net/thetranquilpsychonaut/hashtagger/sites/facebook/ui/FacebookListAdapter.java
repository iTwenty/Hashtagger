package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;
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

    public FacebookListAdapter( Context context, int textViewResourceId, ArrayList<ExpandablePost> posts )
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
