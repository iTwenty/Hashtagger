package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookMediaRow extends SitesListRow implements View.OnClickListener
{
    private ImageView imgvProfileImage;
    private TextView  tvUserNameOrStory;
    private TextView  tvCreatedTime;
    private TextView  tvMessage;
    private ImageView imgvPicture;
    private ImageView imgvPlayButton;
    private Post      post;

    protected FacebookMediaRow( Context context )
    {
        super( context );
    }

    protected FacebookMediaRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookMediaRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_media_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvUserNameOrStory = ( TextView ) findViewById( R.id.tv_user_name_or_story );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        imgvPlayButton = ( ImageView ) findViewById( R.id.imgv_play_button );
        imgvPicture.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.facebook_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        this.post = ( Post ) result;
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, Helper.getFacebookPictureUrl( post.getFrom().getId() ), getResources().getDrawable( R.drawable.drawable_image_loading ), HashtaggerApp.CACHE_DURATION_MS );
        tvUserNameOrStory.setText( post.getStory() == null ? post.getFrom().getName() : post.getStory() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
        tvMessage.setText( post.getMessage() );
        UrlImageViewHelper.setUrlDrawable(
                imgvPicture,
                post.getPicture().toString(),
                null,
                HashtaggerApp.CACHE_DURATION_MS,
                new UrlImageViewCallback()
                {
                    @Override
                    public void onLoaded( ImageView imageView, Bitmap bitmap, String s, boolean b )
                    {
                        if ( "video".equals( post.getType() ) )
                        {
                            imgvPlayButton.setVisibility( VISIBLE );
                        }
                        else
                        {
                            imgvPlayButton.setVisibility( GONE );
                        }
                    }
                }
        );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imgvPicture ) )
        {
            if ( "video".equals( post.getType() ) )
            {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( post.getSource().toString() ) );
                getContext().startActivity( intent );
            }
            else if ( "photo".equals( post.getType() ) )
            {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( post.getPicture().toString().replace( "_s.", "_o." ) ) );
                getContext().startActivity( intent );
            }
        }
    }
}
