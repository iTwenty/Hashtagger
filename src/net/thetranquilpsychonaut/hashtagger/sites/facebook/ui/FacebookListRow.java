package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

/**
 * Created by itwenty on 4/14/14.
 */
public class FacebookListRow extends SitesListRow
{
    private ImageView    imgvProfileImage;
    private TextView     tvUserNameOrStory;
    private TextView     tvCreatedTime;
    private TextView     tvMessage;
    private TextView     tvExpand;
    private ViewAnimator vaAttachmentView;
    private ImageView    imgvPictureCenter;
    private ImageView    imgvPicture;
    private TextView     tvName;
    private TextView     tvDescription;
    private TextView     tvCaption;

    protected FacebookListRow( Context context )
    {
        this( context, null, 0 );
    }

    protected FacebookListRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    protected FacebookListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    public void showRow( Object data )
    {
        final Post post = ( Post ) data;
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, Helper.getFacebookPictureUrl( post.getFrom().getId() ) );
        tvUserNameOrStory.setText( post.getStory() == null ? post.getFrom().getName() : post.getStory() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
        tvMessage.setText( post.getMessage() );
        if ( !"status".equals( post.getType() ) )
        {
            tvExpand.setVisibility( VISIBLE );
        }
        else
        {
            tvExpand.setVisibility( GONE );
        }
    }

    @Override
    protected void init( Context context )
    {
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvUserNameOrStory = ( TextView ) findViewById( R.id.tv_user_name_or_story );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        tvExpand = ( TextView ) findViewById( R.id.tv_expand );
        vaAttachmentView = ( ViewAnimator ) findViewById( R.id.va_attachment_view );
        imgvPictureCenter = ( ImageView ) findViewById( R.id.imgv_picture_center );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        tvName = ( TextView ) findViewById( R.id.tv_name );
        tvDescription = ( TextView ) findViewById( R.id.tv_description );
        tvCaption = ( TextView ) findViewById( R.id.tv_caption );
        vaAttachmentView.setVisibility( GONE );
    }

    @Override
    public void expandRow( Object data )
    {
        ExpandablePost ep = ( ExpandablePost ) data;
        if( ep.isExpanded() )
            return;
        final Post post = ep.getPost();
        clearExpandedData();
        // No need to show anything if post is of type status
        if ( "status".equals( post.getType() ) )
        {
            return;
        }
        // This if will most likely never be true.
        if ( null != post.getObjectId() && null == post.getPicture() )
            return;
        // If object id is not null and we have a picture, show it in center
        vaAttachmentView.setVisibility( VISIBLE );
        vaAttachmentView.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                Uri uri = Uri.parse( post.getLink().toString() );
                HashtaggerApp.app.startActivity( new Intent( Intent.ACTION_VIEW ).setData( uri ).setFlags( Intent.FLAG_ACTIVITY_NEW_TASK ) );
            }
        } );
        if ( null != post.getObjectId() )
        {
            vaAttachmentView.setDisplayedChild( 1 );
            imgvPicture.setImageDrawable( null );
            tvName.setText( "" );
            tvDescription.setText( "" );
            tvCaption.setText( "" );
            UrlImageViewHelper.setUrlDrawable( imgvPictureCenter, post.getPicture().toString(), HashtaggerApp.app.getResources().getDrawable( R.drawable.drawable_image_loading ) );
        }
        // Else we have related info present. Show the info view
        else
        {
            vaAttachmentView.setDisplayedChild( 0 );
            imgvPictureCenter.setImageDrawable( null );
            if ( null != post.getPicture() )
                UrlImageViewHelper.setUrlDrawable( imgvPicture, post.getPicture().toString(), HashtaggerApp.app.getResources().getDrawable( R.drawable.drawable_image_loading ) );
            else
                imgvPicture.setImageDrawable( HashtaggerApp.app.getResources().getDrawable( R.drawable.drawable_image_loading ) );
            tvName.setText( post.getName() );
            tvDescription.setText( null == post.getDescription() ? post.getLink().toString() : post.getDescription() );
            tvCaption.setText( post.getCaption() );
        }
        tvExpand.setText( "Collapse" );
    }

    @Override
    public void collapseRow()
    {
        vaAttachmentView.setVisibility( GONE );
        vaAttachmentView.setOnClickListener( null );
        tvExpand.setText( "Expand" );
    }


    private void clearExpandedData()
    {
        imgvPicture.setImageDrawable( null );
        tvName.setText( "" );
        tvDescription.setText( "" );
        tvCaption.setText( "" );
        imgvPictureCenter.setImageDrawable( null );
    }
}
