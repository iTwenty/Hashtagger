package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListAdapter;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

import java.util.List;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusListAdapter extends SitesListAdapter
{
    public GPlusListAdapter( Context context, int textViewResourceId, List<?> objects )
    {
        super( context, textViewResourceId, objects );
    }

    @Override
    public int getItemViewType( int position )
    {
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
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
