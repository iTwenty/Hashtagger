package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import twitter4j.Status;

/**
 * Created by itwenty on 5/10/14.
 */
public class TwitterDetailActivity extends SitesDetailActivity implements View.OnClickListener
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
        String statusText = status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText();
        tvStatus.setText( Html.fromHtml( Helper.getLinkedStatusText( statusText ) ) );
        tvStatus.setMovementMethod( LinkMovementMethod.getInstance() );
        imgvMediaImage.setOnClickListener( this );
        if ( TwitterListAdapter.getStatusType( this.status ) == TwitterListAdapter.STATUS_TYPE_MEDIA )
        {
            Picasso.with( this )
                    .load( status.getMediaEntities()[0].getMediaURL() + ":large" )
                    .into( imgvMediaImage, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            imgvMediaImage.setVisibility( View.VISIBLE );
                        }

                        @Override
                        public void onError()
                        {

                        }
                    } );
        }
    }

    @Override
    protected TextView getLinkedTextView()
    {
        return tvStatus;
    }

    @Override
    public void onClick( View v )
    {
        Intent intent = new Intent( this, ViewImageActivity.class );
        intent.putExtra( ViewImageActivity.IMAGE_URL_KEY, status.getMediaEntities()[0].getMediaURL() + ":large" );
        startActivity( intent );
    }
}
