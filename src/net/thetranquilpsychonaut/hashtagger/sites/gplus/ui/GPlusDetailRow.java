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
 * Created by itwenty on 5/14/14.
 */
public class GPlusDetailRow extends GPlusListRow implements View.OnClickListener
{
    private GPlusDetailView gPlusDetailView;

    public GPlusDetailRow( Context context )
    {
        super( context );
    }

    public GPlusDetailRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public GPlusDetailRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.gplus_detail_row, this );
        gPlusDetailView = ( GPlusDetailView ) findViewById( R.id.gplus_detail_view );
        gPlusDetailView.setOnClickListener( this );
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
        gPlusDetailView.showDetails( activity );
    }


    @Override
    public void onClick( View v )
    {
        if ( v.equals( gPlusDetailView ) )
        {
            Intent intent = new Intent( Intent.ACTION_VIEW );
            if ( "video".equals( activity.getObject().getAttachments().get( 0 ).getObjectType() ) )
            {
                intent.setData( Uri.parse( activity.getObject().getAttachments().get( 0 ).getEmbed().getUrl() ) );
            }
            else
            {
                intent.setData( Uri.parse( activity.getObject().getAttachments().get( 0 ).getUrl() ) );
            }
            getContext().startActivity( intent );
        }
    }
}
