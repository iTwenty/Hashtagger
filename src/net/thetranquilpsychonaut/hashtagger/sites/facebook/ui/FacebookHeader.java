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
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/11/14.
 */
public class FacebookHeader extends RelativeLayout implements View.OnClickListener
{
    private ImageView imgvProfileImage;
    private TextView  tvUserName;
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
        tvUserName = ( TextView ) findViewById( R.id.tv_user_name );
        tvCreatedTime = ( TextView ) findViewById( R.id.tv_created_time );
        imgvProfileImage.setOnClickListener( this );
    }

    public void showHeader( Post post )
    {
        this.post = post;
        Picasso.with( getContext() )
                .load( Helper.getFacebookProfilePictureUrl( post.getFrom().getId() ) )
                .fit()
                .centerCrop()
                .into( imgvProfileImage );
        tvUserName.setText( post.getFrom().getName() );
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
