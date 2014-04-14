package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/14/14.
 */
public class FacebookListRow
{
    private boolean      isExpanded;
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

    public FacebookListRow( View view )
    {
        imgvProfileImage = ( ImageView ) view.findViewById( R.id.imgv_profile_image );
        tvUserNameOrStory = ( TextView ) view.findViewById( R.id.tv_user_name_or_story );
        tvCreatedTime = ( TextView ) view.findViewById( R.id.tv_created_time );
        tvMessage = ( TextView ) view.findViewById( R.id.tv_message );
        tvExpand = ( TextView ) view.findViewById( R.id.tv_expand );
        vaAttachmentView = ( ViewAnimator ) view.findViewById( R.id.va_attachment_view );
        imgvPictureCenter = ( ImageView ) view.findViewById( R.id.imgv_picture_center );
        imgvPicture = ( ImageView ) view.findViewById( R.id.imgv_picture );
        tvName = ( TextView ) view.findViewById( R.id.tv_name );
        tvDescription = ( TextView ) view.findViewById( R.id.tv_description );
        tvCaption = ( TextView ) view.findViewById( R.id.tv_caption );
        vaAttachmentView.setVisibility( View.GONE );
        isExpanded = false;
    }

    public void showPost( Post post )
    {
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, Helper.getFacebookPictureUrl( post.getFrom().getId() ) );
        tvUserNameOrStory.setText( post.getStory() == null ? post.getFrom().getName() : post.getStory() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
        tvMessage.setText( post.getMessage() );
        if ( !"status".equals( post.getType() ) )
        {
            tvExpand.setVisibility( View.VISIBLE );
        }
        else
        {
            tvExpand.setVisibility( View.GONE );
        }
    }

    public void expandRow( final Post post )
    {
        clearView();
        if ( isExpanded )
            return;
        // No need to show anything if post is of type status
        if ( "status".equals( post.getType() ) )
        {
            return;
        }
        // This if will most likely never be true.
        if ( null != post.getObjectId() && null == post.getPicture() )
            return;
        // If object id is not null and we have a picture, show it in center
        vaAttachmentView.setVisibility( View.VISIBLE );
        vaAttachmentView.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
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
        isExpanded = true;
    }

    public void collapseRow()
    {
        vaAttachmentView.setVisibility( View.GONE );
        vaAttachmentView.setOnClickListener( null );
        tvExpand.setText( "Expand" );
        isExpanded = false;
    }

    public boolean isExpanded()
    {
        return isExpanded;
    }

    private void clearView()
    {
        imgvPicture.setImageDrawable( null );
        tvName.setText( "" );
        tvDescription.setText( "" );
        tvCaption.setText( "" );
        imgvPictureCenter.setImageDrawable( null );
    }
}
