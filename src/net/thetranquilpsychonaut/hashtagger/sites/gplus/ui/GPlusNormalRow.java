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
    private TextView     tvMessage;
    private GPlusButtons gPlusButtons;
    private Activity     activity;

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
        gPlusButtons = ( GPlusButtons ) findViewById( R.id.gplus_buttons );
    }

    @Override
    protected SitesButtons getSitesButtons()
    {
        if ( null == gPlusButtons )
        {
            throw new RuntimeException( "getSitesButtons called before they are ready" );
        }
        return gPlusButtons;
    }

    @Override
    public void updateRow( Object result )
    {
        this.activity = ( Activity ) result;
        tvMessage.setText( activity.getObject().getContent() );
    }
}
