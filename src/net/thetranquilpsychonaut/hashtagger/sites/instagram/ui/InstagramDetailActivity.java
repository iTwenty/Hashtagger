package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.InstagramLikeDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;
import net.thetranquilpsychonaut.hashtagger.widgets.VideoThumbnail;

/**
 * Created by itwenty on 7/1/14.
 */
public class InstagramDetailActivity extends BaseActivity
{
    private static Media media = null;

    private static final String MEDIA_KEY = "media";

    private InstagramHeader   instagramHeader;
    private LinkifiedTextView tvMediaText;
    private VideoThumbnail    videoThumbnail;
    private InstagramButtons  instagramButtons;
    private int               mediaType;

    public static void createAndStartActivity( Media media, Context context )
    {
        InstagramDetailActivity.media = media;
        Intent i = new Intent( context, InstagramDetailActivity.class );
        context.startActivity( i );
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        HashtaggerApp.bus.register( this );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        HashtaggerApp.bus.unregister( this );
        media = null;
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putSerializable( MEDIA_KEY, media );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_instagram_detail );
        instagramHeader = ( InstagramHeader ) findViewById( R.id.instagram_header );
        tvMediaText = ( LinkifiedTextView ) findViewById( R.id.tv_media_text );
        videoThumbnail = ( VideoThumbnail ) findViewById( R.id.video_thumbnail );
        instagramButtons = ( InstagramButtons ) findViewById( R.id.instagram_buttons );
        if ( null != savedInstanceState )
        {
            media = ( Media ) savedInstanceState.getSerializable( MEDIA_KEY );
        }
        // Should never happen
        if ( null == media )
        {
            finish();
        }
        mediaType = InstagramListAdapter.getMediaType( media );
        instagramHeader.updateHeader( media );
        instagramButtons.updateButtons( media );
        tvMediaText.setText( null == media.getCaption() ? "" : media.getCaption().getLinkedText() );
        Picasso.with( this )
                .load( media.getImages().getStandardResolution().getUrl() )
                .into( videoThumbnail.getVideoThumbnail(), new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        videoThumbnail.setVisibility( View.VISIBLE );
                        videoThumbnail.showPlayButton( mediaType == InstagramListAdapter.MEDIA_TYPE_VIDEO );
                    }

                    @Override
                    public void onError()
                    {

                    }
                } );
        videoThumbnail.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                if ( mediaType == InstagramListAdapter.MEDIA_TYPE_VIDEO )
                {
                    Intent i = new Intent( Intent.ACTION_VIEW );
                    i.setData( Uri.parse( media.getVideos().getStandardResolution().getUrl() ) );
                    startActivity( i );
                }
                else
                {
                    ViewAlbumActivity.createAndStartActivity(
                            InstagramDetailActivity.this,
                            media.getUser().getUserName(),
                            Helper.createStringArrayList( media.getImages().getStandardResolution().getUrl() ),
                            0 );
                }
            }
        } );
    }

    @Subscribe
    public void onInstagramLikeDone( InstagramLikeDoneEvent event )
    {
        if ( event.isSuccess() )
        {
            instagramButtons.updateButtons( media );
        }
        else
        {
            Toast.makeText( this,
                    event.getMedia().isUserHasLiked() ?
                            getResources().getString( R.string.str_instagram_delete_like_failed ) :
                            getResources().getString( R.string.str_instagram_post_like_failed ),
                    Toast.LENGTH_SHORT )
                    .show();
        }
    }
}
