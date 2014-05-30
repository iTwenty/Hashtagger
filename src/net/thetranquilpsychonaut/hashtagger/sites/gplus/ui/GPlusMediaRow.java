package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusMediaRow extends GPlusListRow
{
    private GPlusMediaView gPlusMediaView;

    public GPlusMediaRow( Context context )
    {
        super( context );
    }

    public GPlusMediaRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public GPlusMediaRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.gplus_media_row, this );
        gPlusMediaView = ( GPlusMediaView ) findViewById( R.id.gplus_media_view );
        super.init( context );
    }

    @Override
    protected GPlusHeader initGPlusHeader()
    {
        return ( GPlusHeader ) findViewById( R.id.gplus_header );
    }

    @Override
    protected TextView initActivityText()
    {
        return ( TextView ) findViewById( R.id.tv_activity_text );
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
        gPlusMediaView.showMediaThumbnail( activity );
    }
}
