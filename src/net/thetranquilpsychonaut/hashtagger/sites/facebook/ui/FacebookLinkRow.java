package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
public class FacebookLinkRow extends SitesListRow implements View.OnClickListener
{
    private ImageView       imgvProfileImage;
    private TextView        tvUserNameOrStory;
    private TextView        tvCreatedTime;
    private TextView        tvMessage;
    private RelativeLayout  rlLinkWrapper;
    private ImageView       imgvPicture;
    private ImageView       imgvPlayButton;
    private TextView        tvName;
    private TextView        tvDescription;
    private TextView        tvCaption;
    private FacebookButtons facebookButtons;
    private Post            post;

    protected FacebookLinkRow( Context context )
    {
        super( context );
    }

    protected FacebookLinkRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookLinkRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_link_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvUserNameOrStory = ( TextView ) findViewById( R.id.tv_user_name_or_story );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        rlLinkWrapper = ( RelativeLayout ) findViewById( R.id.rl_link_wrapper );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        imgvPlayButton = ( ImageView ) findViewById( R.id.imgv_play_button );
        facebookButtons = ( FacebookButtons ) findViewById( R.id.facebook_buttons );
        tvName = ( TextView ) findViewById( R.id.tv_name );
        tvDescription = ( TextView ) findViewById( R.id.tv_description );
        tvCaption = ( TextView ) findViewById( R.id.tv_caption );
        facebookButtons = ( FacebookButtons ) findViewById( R.id.facebook_buttons );
        rlLinkWrapper.setOnClickListener( this );
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
        this.post = ( Post ) result;
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, Helper.getFacebookPictureUrl( post.getFrom().getId() ), getResources().getDrawable( R.drawable.drawable_image_loading ), HashtaggerApp.CACHE_DURATION_MS );
        tvUserNameOrStory.setText( post.getStory() == null ? post.getFrom().getName() : post.getStory() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
        tvMessage.setText( post.getMessage() );
        if ( null == post.getPicture() )
        {
            imgvPicture.setImageDrawable( getResources().getDrawable( R.drawable.drawable_image_loading ) );
        }
        else
        {
            UrlImageViewHelper.setUrlDrawable( imgvPicture, post.getPicture().toString(), null, HashtaggerApp.CACHE_DURATION_MS );
        }
        tvName.setText( post.getName() );
        tvDescription.setText( post.getDescription() );
        tvCaption.setText( post.getCaption() );
        if ( null == post.getLink() )
        {
            rlLinkWrapper.setVisibility( GONE );
        }
        else
        {
            rlLinkWrapper.setVisibility( VISIBLE );
        }
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( rlLinkWrapper ) )
        {
            if ( null == post.getLink() )
            {
                return;
            }
            Intent intent = new Intent( Intent.ACTION_VIEW );
            intent.setData( Uri.parse( post.getLink().toString() ) );
            getContext().startActivity( intent );
        }
    }
}
