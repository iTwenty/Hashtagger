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
    Context context;

    protected SitesListAdapter( Context context, int textViewResourceId, List<?> objects )
    {
        super( context, textViewResourceId, objects );
        this.context = context;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        SitesListRow sitesListRow = ( SitesListRow ) convertView;
        Object data = getItem( position );
        if( sitesListRow == null )
        {
            sitesListRow = initSitesListRow( context );
            sitesListRow.setTag( sitesListRow );
        }
        else
        {
            sitesListRow = ( SitesListRow ) sitesListRow.getTag();
        }
        sitesListRow.showRow( data );
        return sitesListRow;
    }

    protected abstract SitesListRow initSitesListRow( Context context );
}
