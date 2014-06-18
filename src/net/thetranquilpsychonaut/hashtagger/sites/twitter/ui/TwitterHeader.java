package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

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
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.User;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/8/14.
 */
public class TwitterHeader extends RelativeLayout implements View.OnClickListener
{
    private ImageView imgvProfileImage;
    private TextView  tvName;
    private TextView  tvScreenName;
    private TextView  tvCreatedAt;
    private TextView  tvRetweetName;
    private Status    status;

    public TwitterHeader( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterHeader( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterHeader( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.twitter_header, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvName = ( TextView ) findViewById( R.id.tv_name );
        tvScreenName = ( TextView ) findViewById( R.id.tv_screen_name );
        tvCreatedAt = ( TextView ) findViewById( R.id.tv_created_at );
        tvRetweetName = ( TextView ) findViewById( R.id.tv_retweet_name );
        imgvProfileImage.setOnClickListener( this );
    }

    public void showHeader( Status status )
    {
        this.status = status;
        User user = status.getUser();
        if ( status.isRetweet() )
        {
            tvRetweetName.setText( " of @" + status.getRetweetedStatus().getUser().getScreenName() );
            tvRetweetName.setVisibility( VISIBLE );
        }
        else
        {
            tvRetweetName.setVisibility( GONE );
        }
        Picasso.with( getContext() )
                .load( user.getProfileImageUrl() )
                .fit()
                .centerCrop()
                .into( imgvProfileImage );
        tvName.setText( user.getName() );
        tvScreenName.setText( "@" + user.getScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imgvProfileImage ) )
        {
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setData( Uri.parse( "http://twitter.com/" + status.getUser().getScreenName() ) );
            getContext().startActivity( i );
        }
    }
}
