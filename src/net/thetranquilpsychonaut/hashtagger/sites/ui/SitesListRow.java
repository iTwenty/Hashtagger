package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by itwenty on 4/15/14.
 */
public abstract class SitesListRow extends RelativeLayout
{
    protected boolean isExpanded;

    protected SitesListRow( Context context )
    {
        this( context, null, 0 );
    }

    protected SitesListRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    protected SitesListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        this.isExpanded = false;
    }

    public void expandRow( boolean animate )
    {
        isExpanded = true;
    }

    public abstract void updateRow( final Object data );

    public void collapseRow( boolean animate )
    {
        isExpanded = false;
    }
}
