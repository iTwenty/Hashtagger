package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;

import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public abstract class SitesListAdapter extends ArrayAdapter
{
    Context context;

    protected SitesListAdapter( Context context, int textViewResourceId, List<?> objects )
    {
        super( context, textViewResourceId, objects );
        this.context = context;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        SitesListRow sitesListRow = initSitesListRow( context, position, convertView, parent );
        Object data = getItem( position );
        sitesListRow.updateRow( data );
        if ( null != parent.getTag() )
        {
            int expandedPosition = ( Integer ) parent.getTag();
            if ( position == expandedPosition )
            {
                sitesListRow.expandRow( false );
            }
            else
            {
                if ( sitesListRow.isExpanded )
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

    protected abstract SitesListRow initSitesListRow( Context context, int position, View convertView, ViewGroup parent );

    public abstract void updateTypes( SearchType searchType, List<?> searchResults );
}
