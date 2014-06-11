package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusDetailRow extends GPlusListRow
{
    private GPlusDetailView gPlusDetailView;

    public GPlusDetailRow( Context context )
    {
        super( context );
    }

    public GPlusDetailRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public GPlusDetailRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.gplus_detail_row, this );
        gPlusDetailView = ( GPlusDetailView ) findViewById( R.id.gplus_detail_view );
        super.init( context );
    }

    @Override
    protected GPlusHeader initGPlusHeader()
    {
        return ( GPlusHeader ) findViewById( R.id.gplus_header );
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
    public void updateRow( Object result )
    {
        super.updateRow( result );
        gPlusDetailView.showDetails( activity );
    }
}
