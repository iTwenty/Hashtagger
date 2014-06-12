package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/10/14.
 */
public class TwitterDetailActivity extends BaseActivity
{
    public static final String STATUS_KEY = "status";
    private LinkifiedTextView tvStatusText;
    private TwitterHeader     twitterHeader;
    private Status            status;
    private int               statusType;

    private ViewStub  viewStub;
    private ImageView imgvPhoto;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_twitter_detail );
        setTitle( "Tweet" );
        tvStatusText = ( LinkifiedTextView ) findViewById( R.id.tv_status_text );
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
        tvStatusText.setText( status.getLinkedText() );
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
        final String imageUrl = Helper.getTwitterLargePhotoUrl( status.getEntities().getMedia().get( 0 ).getMediaUrl() );
        Helper.debug( imageUrl );
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
}
