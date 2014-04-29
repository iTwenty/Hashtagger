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
 * Created by itwenty on 4/15/14.
 */
public class TwitterListRow extends SitesListRow
{
    private ImageView         imgvProfileImage;
    private TextView          tvScreenName;
    private TextView          tvCreatedAt;
    private TextView          tvTweetText;
    private TextView          tvExpandHandle;
    private TwitterExpandView twitterExpandView;
    private Status            status;
    private int               statusType;

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
        inflate( context, R.layout.twitter_list_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvScreenName = ( TextView ) findViewById( R.id.tv_screen_name );
        tvCreatedAt = ( TextView ) findViewById( R.id.tv_created_at );
        tvTweetText = ( TextView ) findViewById( R.id.tv_tweet_text );
        tvExpandHandle = ( TextView ) findViewById( R.id.tv_expand_handle );
        twitterExpandView = ( TwitterExpandView ) findViewById( R.id.twitter_expand_view );
    }

    @Override
    public void expandRow( final boolean animate )
    {
        super.expandRow( animate );
        twitterExpandView.expandStatus( status, statusType, animate );
        tvExpandHandle.setText( getExpandHandleText() );
    }

    @Override
    public void updateRow( final Object data )
    {
        this.status = ( Status ) data;
        this.statusType = getStatusType();
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, status.getUser().getProfileImageURL(), R.drawable.drawable_image_loading, HashtaggerApp.CACHE_DURATION_MS );
        tvScreenName.setText( "@" + status.getUser().getScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
        tvTweetText.setText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
        tvExpandHandle.setText( getExpandHandleText() );
    }

    private String getExpandHandleText()
    {
        switch ( statusType )
        {
            case TwitterListAdapter.STATUS_TYPE_MEDIA:
                return isExpanded ? "Hide Photo" : "Show Photo";
            case TwitterListAdapter.STATUS_TYPE_LINK:
                return isExpanded ? "Hide Link" : "Show Link";
            case TwitterListAdapter.STATUS_TYPE_NORMAL:
                return isExpanded ? "Show Less" : "Show More";
        }
        return isExpanded ? "Show Less" : "Show More";
    }


    public int getStatusType()
    {
        boolean hasMedia = status.getMediaEntities().length > 0;
        boolean hasLink = status.getURLEntities().length > 0;
        if ( hasMedia )
        {
            return TwitterListAdapter.STATUS_TYPE_MEDIA;
        }
        else
        {
            if ( hasLink )
            {
                return TwitterListAdapter.STATUS_TYPE_LINK;
            }
            else
            {
                return TwitterListAdapter.STATUS_TYPE_NORMAL;
            }
        }
    }

    @Override
    public void collapseRow( boolean animate )
    {
        super.collapseRow( animate );
        twitterExpandView.collapseStatus( animate );
        tvExpandHandle.setText( getExpandHandleText() );
    }
}
