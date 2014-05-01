package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import twitter4j.Status;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterNormalRow extends SitesListRow
{
    ImageView              imgvProfileImage;
    TextView               tvScreenName;
    TextView               tvCreatedAt;
    TextView               tvTweet;
    TwitterNormalExpandRow twitterNormalExpandRow;

    protected TwitterNormalRow( Context context )
    {
        this( context, null, R.attr.sitesListRowStyle );
    }

    protected TwitterNormalRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, R.attr.sitesListRowStyle );
    }

    protected TwitterNormalRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.twitter_normal_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvScreenName = ( TextView ) findViewById( R.id.tv_screen_name );
        tvCreatedAt = ( TextView ) findViewById( R.id.tv_created_at );
        tvTweet = ( TextView ) findViewById( R.id.tv_tweet );
        twitterNormalExpandRow = ( TwitterNormalExpandRow ) findViewById( R.id.twitter_normal_expand_row );
    }

    @Override
    public void updateRow( Object result )
    {
        Status status = ( Status ) result;
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, status.getUser().getProfileImageURL(), R.drawable.drawable_image_loading, HashtaggerApp.CACHE_DURATION_MS );
        tvScreenName.setText( "@" + status.getUser().getScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
        tvTweet.setText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
    }

    @Override
    public void expandRow( boolean animate )
    {
        super.expandRow( animate );
        twitterNormalExpandRow.expand( animate );
    }

    @Override
    public void collapseRow( boolean animate )
    {
        super.collapseRow( animate );
        twitterNormalExpandRow.collapse( animate );
    }
}
