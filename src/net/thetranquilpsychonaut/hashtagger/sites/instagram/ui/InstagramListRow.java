package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.Actions;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;
import net.thetranquilpsychonaut.hashtagger.widgets.VideoThumbnail;

/**
 * Created by itwenty on 6/25/14.
 */
public class InstagramListRow extends SitesListRow
{
    private LinkifiedTextView tvMediaText;
    private VideoThumbnail    videoThumbnail;
    private Media             media;

    protected InstagramListRow( Context context )
    {
        super( context );
    }

    protected InstagramListRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected InstagramListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.instagram_list_row, this );
        tvMediaText = ( LinkifiedTextView ) findViewById( R.id.tv_media_text );
        videoThumbnail = ( VideoThumbnail ) findViewById( R.id.video_thumbnail );
        videoThumbnail.setOnClickListener( this );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.instagram_buttons );
    }

    @Override
    protected SitesHeader initSitesHeader()
    {
        return ( SitesHeader ) findViewById( R.id.instagram_header );
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
        this.media = ( Media ) result;
        if ( null != media.getCaption() )
        {
            tvMediaText.setText( media.getCaption().getLinkedText() );
        }
        else
        {
            tvMediaText.setText( "" );
        }
        Picasso.with( getContext() )
                .load( media.getImages().getThumbnail().getUrl() )
                .fit()
                .centerCrop()
                .into( videoThumbnail.getVideoThumbnail() );
        videoThumbnail.showPlayButton( TextUtils.equals( "video", media.getType() ) );
    }

    @Override
    public void onClick( View v )
    {
        super.onClick( v );
        if ( v.equals( videoThumbnail ) )
        {
            if ( TextUtils.equals( "video", media.getType() ) )
            {
                Intent i = new Intent( Intent.ACTION_VIEW );
                i.setData( Uri.parse( media.getVideos().getStandardResolution().getUrl() ) );
                getContext().startActivity( i );
            }
            else
            {
                ViewAlbumActivity.createAndStartActivity(
                        getContext(),
                        media.getUser().getUserName(),
                        Helper.createStringArrayList( media.getImages().getStandardResolution().getUrl() ),
                        0 );
            }
        }
    }

    @Override
    protected int getPopupMenuResId()
    {
        return R.menu.instagram_list_row_popup_menu;
    }

    @Override
    protected Uri getResultUrl()
    {
        return UrlModifier.getInstagramMediaUrl( media );
    }

    @Override
    protected String getResultText()
    {
        if ( null == media.getCaption() )
        {
            return null;
        }
        return media.getCaption().getText();
    }

    @Override
    protected boolean onPopupMenuItemClicked( PopupMenu menu, MenuItem item )
    {
        int actionType = Actions.ACTION_INSTAGRAM_LIKE;
        if ( item.equals( menu.getMenu().findItem( R.id.it_view_comments ) ) )
        {
            actionType = Actions.ACTION_INSTAGRAM_COMMENT;
        }
        InstagramActionsFragment fragment = InstagramActionsFragment.newInstance( media, actionType );
        fragment.show( ( ( FragmentActivity ) getContext() ).getSupportFragmentManager(), InstagramActionsFragment.TAG );
        return true;
    }
}
