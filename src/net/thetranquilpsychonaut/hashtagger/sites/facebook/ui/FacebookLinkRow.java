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

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookLinkRow extends SitesListRow implements View.OnClickListener
{
    private FacebookHeader facebookHeader;
    private TextView       tvMessage;
    private TextView       tvLink;
    private Post           post;

    protected FacebookLinkRow( Context context )
    {
        super( context );
    }

    protected FacebookLinkRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookLinkRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_link_row, this );
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        tvLink = ( TextView ) findViewById( R.id.tv_link );
        tvLink.setOnClickListener( this );
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
        tvLink.setText( post.getName() );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( tvLink ) )
        {
            if ( null == post.getLink() )
            {
                return;
            }
            Intent intent = new Intent( Intent.ACTION_VIEW );
            intent.setData( Uri.parse( post.getLink().toString() ) );
            getContext().startActivity( intent );
        }
    }
}
