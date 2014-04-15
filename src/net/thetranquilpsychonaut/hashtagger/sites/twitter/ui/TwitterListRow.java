package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ExpandableStatus;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import twitter4j.Status;

/**
 * Created by itwenty on 4/15/14.
 */
public class TwitterListRow extends SitesListRow
{
    ImageView    imgvProfileImage;
    TextView     tvScreenName;
    TextView     tvCreatedAt;
    TextView     tvTweetText;
    TextView     tvExpand;
    ViewAnimator vaExpandedView;

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
    }

    @Override
    protected void init( Context context )
    {
        LayoutInflater.from( context ).inflate( R.layout.fragment_twitter_list_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvScreenName = ( TextView ) findViewById( R.id.tv_screen_name );
        tvCreatedAt = ( TextView ) findViewById( R.id.tv_created_at );
        tvTweetText = ( TextView ) findViewById( R.id.tv_tweet_text );
        vaExpandedView = ( ViewAnimator ) findViewById( R.id.va_expanded_view );
        tvExpand = ( TextView ) findViewById( R.id.tv_expand );
        vaExpandedView.setVisibility( View.GONE );
    }

    @Override
    public void expandRow( final Object data )
    {
        ExpandableStatus es = ( ExpandableStatus ) data;
        if ( !es.isExpanded() )
            return;
        Status status = es.getStatus();
        vaExpandedView.removeAllViews();
        vaExpandedView.setVisibility( View.VISIBLE );
        if ( status.getMediaEntities().length > 0 )
        {
            TwitterMediaEntity twitterMediaEntity = new TwitterMediaEntity( getContext() );
            vaExpandedView.addView( twitterMediaEntity );
            twitterMediaEntity.showMediaFromStatus( status );
        }
        else if ( status.getURLEntities().length > 0 )
        {
            TwitterLinkEntity twitterLinkEntity = new TwitterLinkEntity( getContext() );
            vaExpandedView.addView( twitterLinkEntity );
            twitterLinkEntity.setLinkFromStatus( status );
        }
    }

    @Override
    public void showRow( final Object data )
    {
        ExpandableStatus es = ( ExpandableStatus ) data;
        Status status = es.getStatus();
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, status.getUser().getProfileImageURL(), R.drawable.drawable_image_loading );
        tvScreenName.setText( "@" + status.getUser().getScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
        tvTweetText.setText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
        if ( status.getMediaEntities().length > 0 || status.getURLEntities().length > 0 )
        {
            tvExpand.setVisibility( VISIBLE );
        }
        else
        {
            tvExpand.setVisibility( GONE );
        }
        if ( es.isExpanded() )
            expandRow( data );
    }

    @Override
    public void collapseRow()
    {
        vaExpandedView.removeAllViews();
        vaExpandedView.setVisibility( GONE );
    }
}
