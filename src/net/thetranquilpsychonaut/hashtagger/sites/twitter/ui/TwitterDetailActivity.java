package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.ArrayList;

/**
 * Created by itwenty on 5/10/14.
 */
public class TwitterDetailActivity extends SitesDetailActivity implements View.OnClickListener
{
    public static final String STATUS_KEY = "status";
    private TextView      tvStatusText;
    private TwitterHeader twitterHeader;
    private Status        status;
    private int           statusType;

    private ViewStub  viewStub;
    private ImageView imgvPhoto;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_twitter_detail );
        setTitle( "Tweet" );
        tvStatusText = ( TextView ) findViewById( R.id.tv_status_text );
        twitterHeader = ( TwitterHeader ) findViewById( R.id.twitter_header );
        viewStub = ( ViewStub ) findViewById( R.id.twitter_view_stub );
        if ( null == getIntent() )
        {
            finish();
        }
        status = ( Status ) getIntent().getSerializableExtra( STATUS_KEY );
        if ( null == status )
        {
            finish();
        }
        this.statusType = TwitterListAdapter.getStatusType( status );
        twitterHeader.showHeader( status );
        String statusText = status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText();
        tvStatusText.setMovementMethod( LinkMovementMethod.getInstance() );
        if ( statusType == TwitterListAdapter.STATUS_TYPE_PHOTO )
        {
            showPhoto( savedInstanceState );
        }
    }

    private void showPhoto( Bundle savedInstanceState )
    {
        viewStub.setLayoutResource( R.layout.twitter_detail_activity_type_photo );
        imgvPhoto = ( ImageView ) viewStub.inflate();
        imgvPhoto.setVisibility( View.GONE );
        final String imageUrl = Helper.getTwitterLargePhotoUrl( status.getEntities().getMedia().get( 0 ).getUrl() );
        Picasso.with( this )
                .load( imageUrl )
                .into( imgvPhoto, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        imgvPhoto.setVisibility( View.VISIBLE );
                    }

                    @Override
                    public void onError()
                    {

                    }
                } );
        imgvPhoto.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                ViewAlbumActivity.createAndStartActivity( v.getContext(), "@" + status.getUser().getScreenName(), Helper.createStringArrayList( imageUrl ), 0 );
            }
        } );
    }

    @Override
    protected TextView getLinkedTextView()
    {
        return tvStatusText;
    }

    @Override
    public void onClick( View v )
    {
        ArrayList<String> imageUrls = new ArrayList<String>( 1 );
        imageUrls.add( Helper.getTwitterLargePhotoUrl( status.getEntities().getMedia().get( 0 ).getUrl() ) );
        ViewAlbumActivity.createAndStartActivity( this, "@" + status.getUser().getScreenName(), imageUrls, 0 );
    }
}
