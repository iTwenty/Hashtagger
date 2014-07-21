package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.Actions;
import net.thetranquilpsychonaut.hashtagger.events.InstagramLikeDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SlidingUpPanelDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;
import net.thetranquilpsychonaut.hashtagger.widgets.VideoThumbnail;

/**
 * Created by itwenty on 7/1/14.
 */
public class InstagramDetailActivity extends SlidingUpPanelDetailActivity
{
    private static Media media = null;

    private static final String MEDIA_KEY = "media";

    private InstagramHeader   instagramHeader;
    private LinkifiedTextView tvMediaText;
    private VideoThumbnail    videoThumbnail;

    public static void createAndStartActivity( Media media, Context context )
    {
        InstagramDetailActivity.media = media;
        Intent i = new Intent( context, InstagramDetailActivity.class );
        context.startActivity( i );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        media = null;
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        InstagramButtons buttons = ( InstagramButtons ) LayoutInflater.from( this ).inflate( R.layout.activity_instagram_detail_buttons, null );
        buttons.updateButtons( media );
        return buttons;
    }

    @Override
    protected View initMainView( Bundle savedInstanceState )
    {
        View v = LayoutInflater.from( this ).inflate( R.layout.activity_instagram_detail, null );
        instagramHeader = ( InstagramHeader ) v.findViewById( R.id.instagram_header );
        tvMediaText = ( LinkifiedTextView ) v.findViewById( R.id.tv_media_text );
        videoThumbnail = ( VideoThumbnail ) v.findViewById( R.id.video_thumbnail );
        if ( null != savedInstanceState )
        {
            media = ( Media ) savedInstanceState.getSerializable( MEDIA_KEY );
        }
        // Should never happen
        if ( null == media )
        {
            finish();
        }
        instagramHeader.updateHeader( media );
        tvMediaText.setText( null == media.getCaption() ? "" : media.getCaption().getLinkedText() );
        Picasso.with( this )
                .load( media.getImages().getStandardResolution().getUrl() )
                .into( videoThumbnail.getVideoThumbnail(), new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        videoThumbnail.setVisibility( View.VISIBLE );
                        videoThumbnail.showPlayButton( TextUtils.equals( "video", media.getType() ) );
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
                if ( TextUtils.equals( "video", media.getType() ) )
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
        return v;
    }

    @Override
    protected String getSitesActionsFragmentTag()
    {
        return InstagramActionsFragment.TAG;
    }

    @Override
    protected Fragment initSitesActionsFragment()
    {
        return InstagramActionsFragment.newInstance( media, Actions.ACTION_INSTAGRAM_LIKE );
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putSerializable( MEDIA_KEY, media );
    }

    @Subscribe
    public void onInstagramLikeDone( InstagramLikeDoneEvent event )
    {
        if ( event.isSuccess() )
        {
            sitesButtons.updateButtons( media );
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
