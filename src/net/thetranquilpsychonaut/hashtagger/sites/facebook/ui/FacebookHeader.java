package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/11/14.
 */
public class FacebookHeader extends RelativeLayout implements View.OnClickListener
{
    private ImageView imgvProfileImage;
    private TextView  tvUserNameOrStory;
    private TextView  tvCreatedTime;
    private Post      post;

    public FacebookHeader( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookHeader( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookHeader( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_header, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvUserNameOrStory = ( TextView ) findViewById( R.id.tv_user_name_or_story );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        imgvProfileImage.setOnClickListener( this );
    }

    public void updateHeader( Post post )
    {
        this.post = post;
        Picasso.with( HashtaggerApp.app ).load( Helper.getFacebookPictureUrl( post.getFrom().getId() ) ).error( R.drawable.drawable_image_loading ).into( imgvProfileImage );
        tvUserNameOrStory.setText( post.getStory() == null ? post.getFrom().getName() : post.getStory() );
        tvCreatedTime.setText( Helper.getFuzzyDateTime( post.getCreatedTime().getTime() ) );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imgvProfileImage ) )
        {
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setData( Uri.parse( "http://facebook.com/" + post.getFrom().getId() ) );
            getContext().startActivity( i );
        }
    }
}
