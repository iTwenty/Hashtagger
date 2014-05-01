package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

/**
 * Created by itwenty on 4/14/14.
 */
public class FacebookListRow extends SitesListRow implements View.OnClickListener
{
    private ImageView          imgvProfileImage;
    private TextView           tvUserNameOrStory;
    private TextView           tvCreatedTime;
    private TextView           tvMessage;
    private FacebookExpandView facebookExpandView;
    private TextView           tvExpandHandle;
    private Post               post;
    private int                postType;

    protected FacebookListRow( Context context )
    {
        this( context, null, R.attr.sitesListRowStyle );
    }

    protected FacebookListRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, R.attr.sitesListRowStyle );
    }

    protected FacebookListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_list_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvUserNameOrStory = ( TextView ) findViewById( R.id.tv_user_name_or_story );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        tvExpandHandle = ( TextView ) findViewById( R.id.tv_expand_handle );
        facebookExpandView = ( FacebookExpandView ) findViewById( R.id.facebook_expand_view );

    }

    @Override
    public void updateRow( final Object data )
    {
        this.post = ( Post ) data;
        this.postType = getPostType();
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, Helper.getFacebookPictureUrl( post.getFrom().getId() ), getResources().getDrawable( R.drawable.drawable_image_loading ), HashtaggerApp.CACHE_DURATION_MS );
        tvUserNameOrStory.setText( post.getStory() == null ? post.getFrom().getName() : post.getStory() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
        tvMessage.setText( post.getMessage() );
        tvExpandHandle.setText( getExpandHandleText() );
    }

    private String getExpandHandleText()
    {
        String strPostType = post.getType();
        if ( "status".equals( strPostType ) )
        {
            return isExpanded ? getResources().getString( R.string.str_fb_show_less ) : getResources().getString( R.string.str_fb_show_more );
        }
        else if ( "photo".equals( strPostType ) )
        {
            return isExpanded ? getResources().getString( R.string.str_fb_hide_photo ) : getResources().getString( R.string.str_fb_show_photo );
        }
        else if ( "video".equals( strPostType ) )
        {
            return isExpanded ? getResources().getString( R.string.str_fb_hide_video ) : getResources().getString( R.string.str_fb_show_video );
        }
        else if ( "link".equals( strPostType ) )
        {
            return isExpanded ? getResources().getString( R.string.str_fb_hide_link ) : getResources().getString( R.string.str_fb_show_link );
        }
        return isExpanded ? getResources().getString( R.string.str_fb_show_less ) : getResources().getString( R.string.str_fb_show_more );
    }

    private int getPostType()
    {
        boolean hasObject = null != post.getObjectId();
        boolean hasDetails = !( "status".equals( post.getType() ) ) && null == post.getObjectId();
        if ( hasObject )
        {
            return FacebookListAdapter.POST_TYPE_OBJECT;
        }
        else if ( hasDetails )
        {
            return FacebookListAdapter.POST_TYPE_DETAILS;
        }
        else
        {
            return FacebookListAdapter.POST_TYPE_NORMAL;
        }
    }

    @Override
    public void expandRow( boolean animate )
    {
        super.expandRow( animate );
        tvMessage.setMaxLines( Integer.MAX_VALUE );
        facebookExpandView.expandPost( post, postType, animate );
        facebookExpandView.setOnClickListener( this );
        tvExpandHandle.setText( getExpandHandleText() );
    }

    @Override
    public void collapseRow( boolean animate )
    {
        super.collapseRow( animate );
        tvMessage.setMaxLines( 10 );
        facebookExpandView.collapsePost( animate );
        facebookExpandView.setOnClickListener( null );
        tvExpandHandle.setText( getExpandHandleText() );
    }

    @Override
    public void onClick( View v )
    {
        Helper.debug( post.getLink().toString() );
    }
}
