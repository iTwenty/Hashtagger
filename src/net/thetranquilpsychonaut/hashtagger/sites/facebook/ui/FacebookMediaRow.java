package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookMediaRow extends FacebookListRow
{
    private FacebookMediaView facebookMediaView;

    protected FacebookMediaRow( Context context )
    {
        super( context );
    }

    protected FacebookMediaRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookMediaRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_media_row, this );
        facebookMediaView = ( FacebookMediaView ) findViewById( R.id.facebook_media_view );
        super.init( context );
    }

    @Override
    protected FacebookHeader initFacebookHeader()
    {
        return ( FacebookHeader ) findViewById( R.id.facebook_header );
    }

    @Override
    protected TextView initPostText()
    {
        return ( TextView ) findViewById( R.id.tv_post_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.facebook_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        facebookMediaView.showMediaThumbnail( post );
    }
}
