package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;
import net.thetranquilpsychonaut.hashtagger.widgets.VideoThumbnail;

/**
 * Created by itwenty on 7/1/14.
 */
public class InstagramDetailActivity extends BaseActivity
{
    private static final String MEDIA_KEY = "media";

    private InstagramHeader   instagramHeader;
    private LinkifiedTextView tvMediaText;
    private VideoThumbnail    videoThumbnail;
    private InstagramButtons  instagramButtons;
    private Media             media;
    private int               mediaType;

    public static void createAndStartActivity( Media media, Context context )
    {
        Intent i = new Intent( context, InstagramDetailActivity.class );
        i.putExtra( MEDIA_KEY, media );
        context.startActivity( i );
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
        media = ( Media ) getIntent().getSerializableExtra( MEDIA_KEY );
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
                        if ( mediaType == InstagramListAdapter.MEDIA_TYPE_VIDEO )
                        {
                            videoThumbnail.showPlayButton( true );
                        }
                    }

                    @Override
                    public void onError()
                    {

                    }
                } );
    }
}
