package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
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
    private ViewStub          viewStub;
    private FacebookButtons   facebookButtons;

    public static void createAndStartActivity( Post post, Context context )
    {
        Intent i = new Intent( context, FacebookDetailActivity.class );
        i.putExtra( POST_KEY, post );
        context.startActivity( i );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_facebook_detail );
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvPostText = ( LinkifiedTextView ) findViewById( R.id.tv_post_text );
        viewStub = ( ViewStub ) findViewById( R.id.facebook_view_stub );
        facebookButtons = ( FacebookButtons ) findViewById( R.id.facebook_buttons );
        if ( null == getIntent() )
        {
            finish();
        }
        this.post = ( Post ) getIntent().getSerializableExtra( POST_KEY );
        if ( null == this.post )
        {
            finish();
        }
        setTitle( post.getFrom().getName() + "'s post" );
        this.postType = FacebookListAdapter.getPostType( this.post );
        facebookHeader.updateHeader( post );
        facebookButtons.updateButtons( post );
        tvPostText.setText( post.getLinkedText() );
        int likesCount = null == post.getLikes() ? 0 : post.getLikes().getData().size();
        int commentsCount = null == post.getComments() ? 0 : post.getComments().getData().size();
        if ( postType == FacebookListAdapter.POST_TYPE_MEDIA )
        {
            showMedia( savedInstanceState );
        }
    }

    private void showMedia( Bundle savedInstancState )
    {
        viewStub.setLayoutResource( R.layout.facebook_detail_activity_type_video );
        FrameLayout temp = ( FrameLayout ) viewStub.inflate();
        final VideoThumbnail videoThumbnail = ( VideoThumbnail ) temp.getChildAt( 0 );
        videoThumbnail.setVisibility( View.GONE );
        final String imageUrl = UrlModifier.getFacebookLargePhotoUrl( post.getPicture() );
        Helper.debug( imageUrl );
        Picasso.with( this )
                .load( imageUrl )
                .error( R.drawable.facebook_icon_flat_170 )
                .into( videoThumbnail.getVideoThumbnail(), new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        videoThumbnail.setVisibility( View.VISIBLE );
                        videoThumbnail.showPlayButton( TextUtils.equals( "video", post.getType() ) );
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
                if ( TextUtils.equals( "photo", post.getType() ) )
                {
                    ViewAlbumActivity.createAndStartActivity( v.getContext(),
                            post.getName(),
                            Helper.createStringArrayList( imageUrl ),
                            0 );
                }
                if ( TextUtils.equals( "video", post.getType() ) )
                {
                    Intent i = new Intent( Intent.ACTION_VIEW );
                    i.setData( Uri.parse( post.getLink() ) );
                    v.getContext().startActivity( i );
                }
            }
        } );
    }
}
