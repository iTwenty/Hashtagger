package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;

/**
 * Created by itwenty on 5/17/14.
 */
public class GPlusLinkRow extends GPlusListRow implements View.OnClickListener
{
    private TextView tvLink;

    protected GPlusLinkRow( Context context )
    {
        super( context );
    }

    protected GPlusLinkRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected GPlusLinkRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.gplus_link_row, this );
        tvLink = ( TextView ) findViewById( R.id.tv_link );
        tvLink.setOnClickListener( this );
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
        tvLink.setText( activity.getObject().getAttachments().get( 0 ).getDisplayName() );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( tvLink ) )
        {
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setData( Uri.parse( activity.getObject().getAttachments().get( 0 ).getUrl() ) );
            getContext().startActivity( i );
        }
    }
}
