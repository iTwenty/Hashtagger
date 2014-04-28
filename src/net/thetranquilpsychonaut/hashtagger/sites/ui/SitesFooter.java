package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 3/29/14.
 */
public class SitesFooter implements View.OnClickListener
{
    public static final String ACTIVE_FOOTER_VIEW_KEY = HashtaggerApp.NAMESPACE + "active_footer_view_key";

    public interface SitesFooterListener
    {
        public void onFooterClicked();
    }

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

    ViewAnimator              vaSitesFooterView;
    SitesFooterView           activeFooterView;
    FooterLoadOlderViewHolder footerLoadOlderViewHolder;
    FooterLoadingViewHolder   footerLoadingViewHolder;
    FooterErrorViewHolder     footerErrorViewHolder;
    protected SitesFooterListener sitesFooterListener;

    public SitesFooter( LayoutInflater inflater, SitesFooterListener listener )
    {
        vaSitesFooterView = ( ViewAnimator ) inflater.inflate( R.layout.footer_view_results_list, null );
        vaSitesFooterView.addView( initLoadOlderView( inflater ), SitesFooterView.LOAD_OLDER.index );
        vaSitesFooterView.addView( initLoadingView( inflater ), SitesFooterView.LOADING.index );
        vaSitesFooterView.addView( initErrorView( inflater ), SitesFooterView.ERROR.index );
        sitesFooterListener = listener;
    }

    public void showFooterView( SitesFooterView sitesFooterView )
    {
        vaSitesFooterView.setDisplayedChild( sitesFooterView.index );
        activeFooterView = sitesFooterView;
    }

    public void appendFooterToList( ListView lv )
    {
        lv.addFooterView( vaSitesFooterView );
    }

    public View initLoadOlderView( LayoutInflater inflater )
    {
        View viewLoadOlder = inflater.inflate( R.layout.footer_view_load_older, null );
        footerLoadOlderViewHolder = new FooterLoadOlderViewHolder();
        footerLoadOlderViewHolder.btnFooterLoadOlderResults = ( Button ) viewLoadOlder.findViewById( R.id.btn_footer_load_older_results );
        footerLoadOlderViewHolder.btnFooterLoadOlderResults.setOnClickListener( this );
        return viewLoadOlder;
    }

    public View initLoadingView( LayoutInflater inflater )
    {
        View viewLoading = inflater.inflate( R.layout.footer_view_loading, null );
        footerLoadingViewHolder = new FooterLoadingViewHolder();
        footerLoadingViewHolder.pgbrFooterLoading = ( ProgressBar ) viewLoading.findViewById( R.id.pgbr_footer_loading );
        return viewLoading;
    }

    public View initErrorView( LayoutInflater inflater )
    {
        View viewError = inflater.inflate( R.layout.footer_view_error, null );
        footerErrorViewHolder = new FooterErrorViewHolder();
        footerErrorViewHolder.tvFooterError = ( TextView ) viewError.findViewById( R.id.tv_footer_error );
        footerErrorViewHolder.btnFooterRetry = ( Button ) viewError.findViewById( R.id.btn_footer_retry );
        footerErrorViewHolder.btnFooterRetry.setOnClickListener( this );
        return viewError;
    }

    @Override
    public void onClick( View v )
    {
        if ( null == sitesFooterListener )
        {
            return;
        }
        sitesFooterListener.onFooterClicked();
    }

    private static class FooterLoadOlderViewHolder
    {
        Button btnFooterLoadOlderResults;
    }

    private static class FooterLoadingViewHolder
    {
        ProgressBar pgbrFooterLoading;
    }

    private static class FooterErrorViewHolder
    {
        TextView tvFooterError;
        Button   btnFooterRetry;
    }
}
