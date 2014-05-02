package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by itwenty on 4/15/14.
 */
public abstract class SitesListRow extends RelativeLayout
{

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
    }

    protected abstract SitesButtons getSitesButtons();

    public void expandRow( boolean animate )
    {
        getSitesButtons().show( animate );
    }

    public abstract void updateRow( final Object result );

    public void collapseRow( boolean animate )
    {
        getSitesButtons().hide( animate );
    }

    public boolean isExpanded()
    {
        return getSitesButtons().isVisible;
    }
}
