package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/31/14.
 */
public class GPlusPhotoRow extends GPlusListRow implements View.OnClickListener
{
    private ImageView imgvThumbnail;

    protected GPlusPhotoRow( Context context )
    {
        super( context );
    }

    protected GPlusPhotoRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected GPlusPhotoRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.gplus_photo_row, this );
        imgvThumbnail = ( ImageView ) findViewById( R.id.imgv_thumbnail );
        imgvThumbnail.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected LinkifiedTextView initActivityText()
    {
        return ( LinkifiedTextView ) findViewById( R.id.tv_activity_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.gplus_buttons );
    }

    @Override
    protected SitesHeader initSitesHeader()
    {
        return ( SitesHeader ) findViewById( R.id.gplus_header );
    }

    @Override
    protected ImageView initPopupMenuAnchor()
    {
        return ( ImageView ) findViewById( R.id.popup_menu_anchor );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        Picasso.with( getContext() )
                .load( UrlModifier.getGPlusSmallPhotoUrl( activity.getObject().getAttachments().get( 0 ).getImage().getUrl() ) )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvThumbnail );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imgvThumbnail ) )
        {
            String imageUrl;
            if ( null != activity.getObject().getAttachments().get( 0 ).getFullImage() )
            {
                imageUrl = activity.getObject().getAttachments().get( 0 ).getFullImage().getUrl();
            }
            else
            {
                imageUrl = activity.getObject().getAttachments().get( 0 ).getImage().getUrl();
            }
            ViewAlbumActivity.createAndStartActivity( getContext(), activity.getActor().getDisplayName(), Helper.createStringArrayList( imageUrl ), 0 );
        }
    }
}
