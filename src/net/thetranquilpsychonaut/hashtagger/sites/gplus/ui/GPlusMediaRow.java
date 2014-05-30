package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusMediaRow extends SitesListRow
{
    private GPlusHeader    gPlusHeader;
    private TextView       tvMessage;
    private GPlusMediaView gPlusMediaView;
    private Activity       activity;

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
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        gPlusHeader = ( GPlusHeader ) findViewById( R.id.gplus_header );
        gPlusMediaView = ( GPlusMediaView ) findViewById( R.id.gplus_media_view );
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
        gPlusMediaView.showMediaThumbnail( activity );
    }
}
