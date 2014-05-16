package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;
import twitter4j.Status;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterMediaRow extends SitesListRow implements View.OnClickListener
{
    private TwitterHeader twitterHeader;
    private TextView      tvTweet;
    private ImageView     imgvMediaThumb;
    private Status        status;

    protected TwitterMediaRow( Context context )
    {
        super( context );
    }

    protected TwitterMediaRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected TwitterMediaRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.twitter_media_row, this );
        twitterHeader = ( TwitterHeader ) findViewById( R.id.twitter_header );
        tvTweet = ( TextView ) findViewById( R.id.tv_tweet );
        imgvMediaThumb = ( ImageView ) findViewById( R.id.imgv_media_thumb );
        imgvMediaThumb.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.twitter_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        this.status = ( Status ) result;
        twitterHeader.updateHeader( status );
        tvTweet.setText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
        Picasso.with( getContext() ).load( status.getMediaEntities()[0].getMediaURL() + ":thumb" ).error( R.drawable.drawable_image_loading ).into( imgvMediaThumb );
    }

    @Override
    public void onClick( View v )
    {
        Intent intent = new Intent( getContext(), ViewImageActivity.class );
        intent.putExtra( ViewImageActivity.IMAGE_URL_KEY, status.getMediaEntities()[0].getMediaURL() + ":large" );
        getContext().startActivity( intent );
    }
}
