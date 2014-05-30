package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookMediaRow extends SitesListRow
{
    private FacebookHeader    facebookHeader;
    private TextView          tvMessage;
    private FacebookMediaView facebookMediaView;
    private Post              post;

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
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        facebookMediaView = ( FacebookMediaView ) findViewById( R.id.facebook_media_view );
        super.init( context );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.facebook_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        this.post = ( Post ) result;
        facebookHeader.showHeader( post );
        tvMessage.setText( post.getMessage() );
        facebookMediaView.showMediaThumbnail( post );
    }
}
