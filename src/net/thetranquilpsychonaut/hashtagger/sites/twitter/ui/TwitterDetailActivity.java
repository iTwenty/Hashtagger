package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import twitter4j.Status;

/**
 * Created by itwenty on 5/10/14.
 */
public class TwitterDetailActivity extends BaseActivity
{
    public static final String STATUS_KEY = "status";
    private Status        status;
    private TextView      tvStatus;
    private TwitterHeader twitterHeader;
    private ImageView     imgvMediaImage;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_twitter_detail );
        setTitle( "Tweet" );
        tvStatus = ( TextView ) findViewById( R.id.tv_status );
        twitterHeader = ( TwitterHeader ) findViewById( R.id.twitter_header );
        imgvMediaImage = ( ImageView ) findViewById( R.id.imgv_media_image );
        status = ( Status ) getIntent().getSerializableExtra( STATUS_KEY );
        twitterHeader.updateHeader( status );
        String statusHtml = Helper.getLinkedTweetText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
        tvStatus.setText( Helper.stripUnderlines( new SpannableString( Html.fromHtml( statusHtml ) ) ) );
        tvStatus.setMovementMethod( LinkMovementMethod.getInstance() );
        if ( status.getMediaEntities().length > 0 )
        {
            imgvMediaImage.setVisibility( View.VISIBLE );
            Picasso.with( this ).load( status.getMediaEntities()[0].getMediaURL() + ":large" ).into( imgvMediaImage );
        }
    }
}
