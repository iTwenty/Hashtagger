package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
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
    }

    public void updateHeader( Status status )
    {
        User user = status.getUser();
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, user.getProfileImageURL(), R.drawable.drawable_image_loading, HashtaggerApp.CACHE_DURATION_MS );
        tvName.setText( user.getName() );
        tvScreenName.setText( "@" + user.getScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
    }
}
