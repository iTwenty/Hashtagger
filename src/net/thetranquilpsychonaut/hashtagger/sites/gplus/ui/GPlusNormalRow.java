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
public class GPlusNormalRow extends SitesListRow
{
    private GPlusHeader gPlusHeader;
    private TextView    tvMessage;
    private Activity    activity;

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
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        gPlusHeader = ( GPlusHeader ) findViewById( R.id.gplus_header );
        super.init( context );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.gplus_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        this.activity = ( Activity ) result;
        gPlusHeader.showHeader( activity );
        tvMessage.setText( activity.getObject().getOriginalContent() );
    }
}
