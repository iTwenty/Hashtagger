package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/7/14.
 */
public class GPlusNormalRow extends GPlusListRow
{
    protected GPlusNormalRow( Context context )
    {
        super( context );
    }

    protected GPlusNormalRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected GPlusNormalRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.gplus_normal_row, this );
        super.init( context );
    }

    @Override
    protected LinkifiedTextView initActivityText()
    {
        return ( LinkifiedTextView ) findViewById( R.id.tv_activity_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.gplus_buttons );
    }

    @Override
    protected SitesHeader initSitesHeader()
    {
        return ( SitesHeader ) findViewById( R.id.gplus_header );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
    }
}
