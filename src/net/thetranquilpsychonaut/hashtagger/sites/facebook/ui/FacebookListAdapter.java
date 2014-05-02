package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
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
    public int getItemViewType( int position )
    {
        return 1;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    protected SitesListRow getSitesListRow( Context context, int position, View convertView, ViewGroup parent )
    {
        return null;
    }

    @Override
    public void updateTypes( SearchType searchType, List<?> searchResults )
    {

    }

    @Override
    public void clearTypes()
    {

    }

    @Override
    public void initTypes( Bundle savedInstanceState )
    {

    }

    @Override
    public void saveTypes( Bundle outState )
    {

    }
}
