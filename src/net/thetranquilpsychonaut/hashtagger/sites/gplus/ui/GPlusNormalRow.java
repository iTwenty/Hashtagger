package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

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
    protected GPlusHeader initGPlusHeader()
    {
        return ( GPlusHeader ) findViewById( R.id.gplus_header );
    }

    @Override
    protected TextView initActivityText()
    {
        return ( TextView ) findViewById( R.id.tv_message );
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
    }
}
