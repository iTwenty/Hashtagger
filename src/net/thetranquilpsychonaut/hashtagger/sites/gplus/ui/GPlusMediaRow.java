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
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusMediaRow extends SitesListRow implements View.OnClickListener
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
        gPlusMediaView.setOnClickListener( this );
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
        gPlusMediaView.updateMedia( activity );
    }

    @Override
    public void onClick( View v )
    {
        String objectType = activity.getObject().getAttachments().get( 0 ).getObjectType();
        if ( "photo".equals( objectType ) )
        {
            Intent i = new Intent( getContext(), ViewImageActivity.class );
            i.putExtra( ViewImageActivity.IMAGE_URL_KEY, activity.getObject().getAttachments().get( 0 ).getImage().getUrl() );
            getContext().startActivity( i );
        }
        else if ( "video".equals( objectType ) )
        {
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setData( Uri.parse( activity.getObject().getAttachments().get( 0 ).getUrl() ) );
            getContext().startActivity( i );
        }
    }
}
