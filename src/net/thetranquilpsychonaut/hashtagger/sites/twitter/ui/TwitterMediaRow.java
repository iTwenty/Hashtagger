package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import twitter4j.Status;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterMediaRow extends SitesListRow
{
    ImageView      imgvProfileImage;
    TextView       tvScreenName;
    TextView       tvCreatedAt;
    TextView       tvTweet;
    ImageView      imgvMediaThumb;
    TwitterButtons twitterButtons;

    protected TwitterMediaRow( Context context )
    {
        this( context, null, R.attr.sitesListRowStyle );
    }

    protected TwitterMediaRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, R.attr.sitesListRowStyle );
    }

    protected TwitterMediaRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.twitter_media_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image_media );
        tvScreenName = ( TextView ) findViewById( R.id.tv_screen_name_media );
        tvCreatedAt = ( TextView ) findViewById( R.id.tv_created_at_media );
        tvTweet = ( TextView ) findViewById( R.id.tv_tweet_media );
        imgvMediaThumb = ( ImageView ) findViewById( R.id.imgv_media_thumb_media );
        twitterButtons = ( TwitterButtons ) findViewById( R.id.twitter_buttons_media );
    }

    @Override
    protected SitesButtons getSitesButtons()
    {
        if ( null == twitterButtons )
        {
            throw new RuntimeException( "getSitesButtons called before they are ready." );
        }
        return twitterButtons;
    }

    @Override
    public void updateRow( Object result )
    {
        Status status = ( Status ) result;
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, status.getUser().getProfileImageURL(), R.drawable.drawable_image_loading, HashtaggerApp.CACHE_DURATION_MS );
        tvScreenName.setText( "@" + status.getUser().getScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
        tvTweet.setText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
        UrlImageViewHelper.setUrlDrawable( imgvMediaThumb, status.getMediaEntities()[0].getMediaURL() + ":thumb", R.drawable.drawable_image_loading, HashtaggerApp.CACHE_DURATION_MS );
    }
}
