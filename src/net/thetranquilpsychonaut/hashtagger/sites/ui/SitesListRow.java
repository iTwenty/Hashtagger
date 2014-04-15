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
        init( context );
    }

    protected abstract void init( Context context );

    public abstract void expandRow( final Object data );

    public abstract void showRow( final Object data );

    public abstract void collapseRow();
}
