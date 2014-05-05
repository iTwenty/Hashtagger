package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
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
public class TwitterLinkRow extends SitesListRow implements View.OnClickListener
{
    private ImageView      imgvProfileImage;
    private TextView       tvScreenName;
    private TextView       tvCreatedAt;
    private TextView       tvTweet;
    private TextView       tvTwitterLink;
    private TwitterButtons twitterButtons;
    private Status         status;

    protected TwitterLinkRow( Context context )
    {
        super( context );
    }

    protected TwitterLinkRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected TwitterLinkRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.twitter_link_row, this );
        imgvProfileImage = ( ImageView ) findViewById( R.id.imgv_profile_image );
        tvScreenName = ( TextView ) findViewById( R.id.tv_screen_name );
        tvCreatedAt = ( TextView ) findViewById( R.id.tv_created_at );
        tvTweet = ( TextView ) findViewById( R.id.tv_tweet );
        tvTwitterLink = ( TextView ) findViewById( R.id.tv_twitter_link );
        twitterButtons = ( TwitterButtons ) findViewById( R.id.twitter_buttons );
        tvTwitterLink.setOnClickListener( this );
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
        this.status = ( Status ) result;
        UrlImageViewHelper.setUrlDrawable( imgvProfileImage, status.getUser().getProfileImageURL(), R.drawable.drawable_image_loading, HashtaggerApp.CACHE_DURATION_MS );
        tvScreenName.setText( "@" + status.getUser().getScreenName() );
        tvCreatedAt.setText( Helper.getFuzzyDateTime( status.getCreatedAt().getTime() ) );
        tvTweet.setText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
        tvTwitterLink.setText( status.getURLEntities()[0].getExpandedURL() );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( tvTwitterLink ) )
        {
            Intent intent = new Intent( Intent.ACTION_VIEW );
            intent.setData( Uri.parse( status.getURLEntities()[0].getExpandedURL() ) );
            getContext().startActivity( intent );

        }
    }
}
