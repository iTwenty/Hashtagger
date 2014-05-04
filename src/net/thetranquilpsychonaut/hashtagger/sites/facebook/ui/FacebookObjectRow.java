package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookObjectRow extends SitesListRow
{
    private ImageView       imgvProfileImage;
    private TextView        tvUserNameOrStory;
    private TextView        tvCreatedTime;
    private TextView        tvMessage;
    private ImageView       imgvPicture;
    private ImageView       imgvPlayButton;
    private FacebookButtons facebookButtons;

    protected FacebookObjectRow( Context context )
    {
        super( context );
    }

    protected FacebookObjectRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookObjectRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_object_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvUserNameOrStory = ( TextView ) findViewById( R.id.tv_user_name_or_story );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        imgvPlayButton = ( ImageView ) findViewById( R.id.imgv_play_button );
        facebookButtons = ( FacebookButtons ) findViewById( R.id.facebook_buttons );
    }

    @Override
    protected SitesButtons getSitesButtons()
    {
        if ( null == facebookButtons )
        {
            throw new RuntimeException( "getSitesButtons called before they are ready." );
        }
        return facebookButtons;
    }

    @Override
    public void updateRow( Object result )
    {
        final Post post = ( Post ) result;
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
}
