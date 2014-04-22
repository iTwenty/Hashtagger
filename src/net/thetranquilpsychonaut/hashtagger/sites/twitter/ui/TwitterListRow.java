package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import twitter4j.Status;

/**
 * Created by itwenty on 4/15/14.
 */
public class TwitterListRow extends SitesListRow
{
    private ImageView         imgvProfileImage;
    private TextView          tvScreenName;
    private TextView          tvCreatedAt;
    private TextView          tvTweetText;
    private TwitterExpandView twitterExpandView;

    public TwitterListRow( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterListRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        LayoutInflater.from( context ).inflate( R.layout.fragment_twitter_list_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvScreenName = ( TextView ) findViewById( R.id.tv_screen_name );
        tvCreatedAt = ( TextView ) findViewById( R.id.tv_created_at );
        tvTweetText = ( TextView ) findViewById( R.id.tv_tweet_text );
        twitterExpandView = ( TwitterExpandView ) findViewById( R.id.twitter_expand_view );
    }

    @Override
    public void expandRow( final Object data, final boolean animate )
    {
        super.expandRow( data, animate );
        final Status status = ( Status ) data;
        twitterExpandView.expandStatus( status, animate );
    }

    @Override
    public void updateRow( final Object data )
    {
        final Status status = ( Status ) data;
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, status.getUser().getProfileImageURL(), R.drawable.drawable_image_loading, HashtaggerApp.CACHE_DURATION_MS );
        tvScreenName.setText( "@" + status.getUser().getScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
        tvTweetText.setText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
    }

    @Override
    public void collapseRow( boolean animate )
    {
        super.collapseRow( animate );
        twitterExpandView.collapseStatus( animate );
    }
}
