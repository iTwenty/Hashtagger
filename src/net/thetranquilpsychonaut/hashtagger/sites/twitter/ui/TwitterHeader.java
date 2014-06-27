package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.User;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/8/14.
 */
public class TwitterHeader extends SitesHeader
{
    private TextView  tvName;
    private TextView  tvScreenName;
    private TextView  tvCreatedAt;
    private TextView  tvRetweetName;
    private Status    status;

    public TwitterHeader( Context context )
    {
        super( context );
    }

    public TwitterHeader( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public TwitterHeader( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.twitter_header, this );
        tvName = ( TextView ) findViewById( R.id.tv_name );
        tvScreenName = ( TextView ) findViewById( R.id.tv_screen_name );
        tvCreatedAt = ( TextView ) findViewById( R.id.tv_created_at );
        tvRetweetName = ( TextView ) findViewById( R.id.tv_retweet_name );
    }

    @Override
    protected ImageView initProfileImage()
    {
        return ( ImageView ) findViewById( R.id.imgv_profile_image );
    }

    @Override
    protected String getProfileUrl()
    {
        return "http://twitter.com/" + status.getUser().getScreenName();
    }

    @Override
    protected void updateHeader( Object result )
    {
        this.status = ( Status ) result;
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
                .into( profileImage );
        tvName.setText( user.getName() );
        tvScreenName.setText( "@" + user.getScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
    }
}
