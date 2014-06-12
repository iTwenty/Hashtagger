package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;
import net.thetranquilpsychonaut.hashtagger.widgets.VideoThumbnail;

/**
 * Created by itwenty on 5/18/14.
 */
public class FacebookDetailActivity extends BaseActivity
{
    public static final String POST_KEY = "post";

    private FacebookHeader    facebookHeader;
    private LinkifiedTextView tvPostText;
    private Post              post;
    private int               postType;

    private ViewStub       viewStub;
    private VideoThumbnail videoThumbnail;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_facebook_detail );
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvPostText = ( LinkifiedTextView ) findViewById( R.id.tv_post_text );
        viewStub = ( ViewStub ) findViewById( R.id.facebook_view_stub );
        if ( null == getIntent() )
        {
            finish();
        }
        this.post = ( Post ) getIntent().getSerializableExtra( POST_KEY );
        if ( null == this.post )
        {
            finish();
        }
        this.postType = FacebookListAdapter.getPostType( this.post );
        facebookHeader.showHeader( post );
        tvPostText.setText( post.getLinkedText() );
        if ( postType == FacebookListAdapter.POST_TYPE_MEDIA )
        {
            showMedia( savedInstanceState );
        }
    }

    private void showMedia( Bundle savedInstancState )
    {
        viewStub.setLayoutResource( R.layout.facebook_detail_activity_type_video );
        FrameLayout temp = ( FrameLayout ) viewStub.inflate();
        videoThumbnail = ( VideoThumbnail ) temp.getChildAt( 0 );
        videoThumbnail.setVisibility( View.GONE );
        final String imageUrl = Helper.getFacebookLargePhotoUrl( post.getPicture() );
        Picasso.with( this )
                .load( imageUrl )
                .error( R.drawable.facebook_icon_plain )
                .centerCrop()
                .fit()
                .into( videoThumbnail.getVideoThumbnail(), new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        videoThumbnail.setVisibility( View.VISIBLE );
                        if ( "photo".equals( post.getType() ) )
                        {
                            videoThumbnail.showPlayButton( false );
                        }
                        else if ( "video".equals( post.getType() ) )
                        {
                            videoThumbnail.showPlayButton( true );
                        }
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
                if ( "photo".equals( post.getType() ) )
                {
                    ViewAlbumActivity.createAndStartActivity( v.getContext(),
                            post.getName(),
                            Helper.createStringArrayList( imageUrl ),
                            0 );
                }
                if ( "video".equals( post.getType() ) )
                {
                    Intent i = new Intent( Intent.ACTION_VIEW );
                    i.setData( Uri.parse( post.getLink() ) );
                    v.getContext().startActivity( i );
                }
            }
        } );
    }
}
