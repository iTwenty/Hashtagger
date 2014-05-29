package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public abstract class SitesListAdapter extends ArrayAdapter
{
    protected List<Integer> resultTypes;

    protected SitesListAdapter( Context context, int textViewResourceId, List<?> objects, List<Integer> resultTypes )
    {
        super( context, textViewResourceId, objects );
        this.resultTypes = resultTypes;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        SitesListRow sitesListRow = getSitesListRow( getContext(), position, convertView, parent );
        Object data = getItem( position );
        sitesListRow.updateRow( data );
        sitesListRow.sitesButtons.setTag( position );
        if ( null != parent.getTag() )
        {
            int expandedPosition = ( Integer ) parent.getTag();
            if ( position == expandedPosition )
            {
                sitesListRow.expandRow( data, false, null );
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
}
