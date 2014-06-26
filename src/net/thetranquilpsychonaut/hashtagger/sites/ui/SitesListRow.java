package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/15/14.
 */
public abstract class SitesListRow extends RelativeLayout
{
    protected SitesButtons sitesButtons;
    protected SitesHeader sitesHeader;

    protected SitesListRow( Context context )
    {
        this( context, null, R.attr.sitesListRowStyle );
    }

    protected SitesListRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, R.attr.sitesListRowStyle );
    }

    protected SitesListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        init( context );
        sitesButtons = initSitesButtons();
        sitesHeader = initSitesHeader();
        if ( null == sitesButtons || null == sitesHeader )
        {
            throw new RuntimeException( "Either SitesButtons or SitesHeader is null." );
        }
    }

    protected abstract void init( Context context );

    protected abstract SitesButtons initSitesButtons();

    protected abstract SitesHeader initSitesHeader();

    public void expandRow( final Object result, boolean animate, AnimatorListenerAdapter adapter )
    {
        sitesButtons.show( result, animate, adapter );
    }

    public void updateRow( final Object result )
    {
        sitesHeader.updateHeader( result );
    }

    public void collapseRow( boolean animate )
    {
        sitesButtons.hide( animate );
    }

    public boolean isExpanded()
    {
        return sitesButtons.mIsVisible;
    }
}
