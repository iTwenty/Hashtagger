package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;

/**
 * Created by itwenty on 7/3/14.
 */
public class TwitterRetweetView extends RelativeLayout
{
    private ImageView retweetImage;
    private TextView  retweetName;
    private TextView  retweetScreenName;

    public TwitterRetweetView( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterRetweetView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterRetweetView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.twitter_retweet_view, this );
        retweetImage = ( ImageView ) findViewById( R.id.retweet_image );
        retweetName = ( TextView ) findViewById( R.id.retweet_name );
        retweetScreenName = ( TextView ) findViewById( R.id.retweet_screen_name );
    }

    public void update( Status status )
    {
        Picasso.with( getContext() )
                .load( status.getUser().getProfileImageUrl() )
                .fit()
                .centerCrop()
                .into( retweetImage );
        retweetName.setText( status.getUser().getName() );
        retweetScreenName.setText( "@" + status.getUser().getScreenName() );
    }
}
