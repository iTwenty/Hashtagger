package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public abstract class SitesListAdapter extends ArrayAdapter
{
    private static final String RESULT_TYPES_KEY = "result_types";
    Context context;
    protected List<Integer> resultTypes;

    protected SitesListAdapter( Context context, int textViewResourceId, List<?> objects )
    {
        super( context, textViewResourceId, objects );
        this.context = context;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        SitesListRow sitesListRow = getSitesListRow( context, position, convertView, parent );
        Object data = getItem( position );
        sitesListRow.updateRow( data );
        if ( null != parent.getTag() )
        {
            int expandedPosition = ( Integer ) parent.getTag();
            if ( position == expandedPosition )
            {
                sitesListRow.expandRow( data, false );
            }
            else
            {
                if ( sitesListRow.isExpanded() )
                {
                    sitesListRow.collapseRow( false );
                }
            }
        }
        return sitesListRow;
    }

    @Override
    public abstract int getItemViewType( int position );

    @Override
    public abstract int getViewTypeCount();

    protected abstract SitesListRow getSitesListRow( Context context, int position, View convertView, ViewGroup parent );

    public abstract void updateTypes( SearchType searchType, List<?> searchResults );

    public void clearTypes()
    {
        resultTypes.clear();
    }

    public void initTypes( Bundle savedInstanceState )
    {

        if ( null != savedInstanceState )
        {
            resultTypes = ( List<Integer> ) savedInstanceState.getSerializable( RESULT_TYPES_KEY );
        }
        else
        {
            resultTypes = new ArrayList<Integer>();
        }
    }

    public void saveTypes( Bundle outState )
    {
        outState.putSerializable( RESULT_TYPES_KEY, ( java.io.Serializable ) resultTypes );
    }
}
