package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import twitter4j.Status;
import twitter4j.User;

/**
 * Created by itwenty on 5/8/14.
 */
public class TwitterHeader extends RelativeLayout
{
    private ImageView imgvProfileImage;
    private TextView  tvName;
    private TextView  tvScreenName;
    private TextView  tvCreatedAt;
    private TextView  tvRetweetName;

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
    }

    public void updateHeader( Status status )
    {
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
        Picasso.with( HashtaggerApp.app ).load( user.getProfileImageURL() ).error( R.drawable.drawable_image_loading ).into( imgvProfileImage );
        tvName.setText( user.getName() );
        tvScreenName.setText( null == status.getInReplyToScreenName() ? "@" + user.getScreenName() : "@" + user.getScreenName() + " in reply to @" + status.getInReplyToScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
    }
}
