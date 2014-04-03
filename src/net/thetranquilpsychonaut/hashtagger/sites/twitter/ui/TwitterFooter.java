package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesFooter;

/**
 * Created by itwenty on 3/29/14.
 */
public class TwitterFooter extends SitesFooter implements View.OnClickListener
{
    FooterLoadOlderViewHolder footerLoadOlderViewHolder;
    FooterLoadingViewHolder   footerLoadingViewHolder;
    FooterErrorViewHolder     footerErrorViewHolder;

    public TwitterFooter( LayoutInflater inflater )
    {
        super( inflater );
    }

    @Override
    protected View getFooterView( SitesFooterView sitesFooterView, LayoutInflater inflater )
    {
        switch ( sitesFooterView )
        {
            case LOAD_OLDER:
                return getLoadOlderView( inflater );
            case LOADING:
                return getLoadingView( inflater );
            case ERROR:
                return getErrorView( inflater );
        }
        return null;
    }

    private View getLoadOlderView( LayoutInflater inflater )
    {
        View viewLoadOlder = inflater.inflate( R.layout.footer_view_load_older, null );
        footerLoadOlderViewHolder = new FooterLoadOlderViewHolder();
        footerLoadOlderViewHolder.btnFooterLoadOlderResults = ( Button ) viewLoadOlder.findViewById( R.id.btn_footer_load_older_results );
        footerLoadOlderViewHolder.btnFooterLoadOlderResults.setOnClickListener( this );
        return viewLoadOlder;
    }

    private View getLoadingView( LayoutInflater inflater )
    {
        View viewLoading = inflater.inflate( R.layout.footer_view_loading, null );
        footerLoadingViewHolder = new FooterLoadingViewHolder();
        footerLoadingViewHolder.pgbrFooterLoading = ( ProgressBar ) viewLoading.findViewById( R.id.pgbr_footer_loading );
        return viewLoading;
    }

    private View getErrorView( LayoutInflater inflater )
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
            return;
        if ( v.equals( footerLoadOlderViewHolder.btnFooterLoadOlderResults ) )
            sitesFooterListener.onLoadOlderResultsClicked();
        else if ( v.equals( footerErrorViewHolder.btnFooterRetry ) )
            sitesFooterListener.onRetryClicked();
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
