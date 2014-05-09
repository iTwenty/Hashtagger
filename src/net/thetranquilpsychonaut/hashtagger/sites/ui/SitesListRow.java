package net.thetranquilpsychonaut.hashtagger.sites.ui;

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
    }

    protected void init( Context context )
    {
        sitesButtons = initSitesButtons();
    }

    protected abstract SitesButtons initSitesButtons();

    public void expandRow( final Object result, boolean animate )
    {
        if ( null == sitesButtons )
        {
            return;
        }
        sitesButtons.show( result, animate );
    }

    public abstract void updateRow( final Object result );

    public void collapseRow( boolean animate )
    {
        if ( null == sitesButtons )
        {
            return;
        }
        sitesButtons.hide( animate );
    }

    public boolean isExpanded()
    {
        if ( null == sitesButtons )
        {
            return false;
        }
        return sitesButtons.isVisible;
    }
}
