package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookDetailRow extends FacebookListRow implements View.OnClickListener
{
    private FacebookDetailView facebookDetailView;

    protected FacebookDetailRow( Context context )
    {
        super( context );
    }

    protected FacebookDetailRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookDetailRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_detail_row, this );
        facebookDetailView = ( FacebookDetailView ) findViewById( R.id.facebook_detail_view );
        facebookDetailView.setOnClickListener( this );
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
        facebookDetailView.showDetails( post );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( facebookDetailView ) )
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
