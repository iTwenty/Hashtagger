package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookMediaRow extends SitesListRow implements View.OnClickListener
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
        facebookMediaView.setOnClickListener( this );
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
        facebookHeader.updateHeader( post );
        tvMessage.setText( post.getMessage() );
        facebookMediaView.updateMedia( post );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( facebookMediaView ) )
        {
            if ( "video".equals( post.getType() ) )
            {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( post.getSource().toString() ) );
                getContext().startActivity( intent );
            }
            else if ( "photo".equals( post.getType() ) )
            {
                Intent intent = new Intent( getContext(), ViewImageActivity.class );
                intent.putExtra( ViewImageActivity.IMAGE_URL_KEY, Helper.getFacebookLargeMediaUrl( post.getPicture().toString() ) );
                getContext().startActivity( intent );
            }
        }
    }
}
