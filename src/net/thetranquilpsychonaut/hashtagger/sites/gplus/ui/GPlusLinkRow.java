package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

/**
 * Created by itwenty on 5/17/14.
 */
public class GPlusLinkRow extends SitesListRow implements View.OnClickListener
{
    private GPlusHeader gPlusHeader;
    private TextView    tvMessage;
    private TextView    tvLink;
    private Activity    activity;

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
        gPlusHeader = ( GPlusHeader ) findViewById( R.id.gplus_header );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        tvLink = ( TextView ) findViewById( R.id.tv_link );
        tvLink.setOnClickListener( this );
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
        gPlusHeader.updateHeader( activity );
        tvMessage.setText( activity.getObject().getOriginalContent() );
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
