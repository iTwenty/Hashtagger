package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ViewAnimator;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 3/29/14.
 */
public abstract class SitesFooter
{
    public static enum SitesFooterView
    {
        LOAD_OLDER( 0 ), LOADING( 1 ), ERROR( 2 );

        private int index;

        SitesFooterView( int index )
        {
            this.index = index;
        }

        public int getIndex()
        {
            return index;
        }
    }

    ViewAnimator vaSitesFooterView;
    View         viewFooterLoadOlder;
    View         viewFooterLoading;
    View         viewFooterError;

    public SitesFooter( LayoutInflater inflater )
    {
        vaSitesFooterView = ( ViewAnimator ) inflater.inflate( R.layout.footer_view_results_list, null );
        viewFooterLoadOlder = getFooterView( SitesFooterView.LOAD_OLDER, inflater );
        viewFooterLoading = getFooterView( SitesFooterView.LOADING, inflater );
        viewFooterError = getFooterView( SitesFooterView.ERROR, inflater );
        vaSitesFooterView.addView( viewFooterLoadOlder, SitesFooterView.LOAD_OLDER.index );
        vaSitesFooterView.addView( viewFooterLoading, SitesFooterView.LOADING.index );
        vaSitesFooterView.addView( viewFooterError, SitesFooterView.ERROR.index );
    }

    protected abstract View getFooterView( SitesFooterView sitesFooterView, LayoutInflater inflater );

    public void showFooterView( SitesFooterView sitesFooterView )
    {
        vaSitesFooterView.setDisplayedChild( sitesFooterView.index );
    }

    public void appendFooterToList( ListView lv )
    {
        lv.addFooterView( vaSitesFooterView );
    }
}
