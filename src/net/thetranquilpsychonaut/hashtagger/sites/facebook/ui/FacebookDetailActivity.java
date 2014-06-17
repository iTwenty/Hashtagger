package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.FacebookActionClickedEvent;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;
import net.thetranquilpsychonaut.hashtagger.widgets.VideoThumbnail;

/**
 * Created by itwenty on 5/18/14.
 */
public class FacebookDetailActivity extends SitesDetailActivity
{
    public static final String POST_KEY = "post";

    private FacebookHeader          facebookHeader;
    private LinkifiedTextView       tvPostText;
    private FacebookActionsFragment facebookActionsFragment;
    private Post                    post;
    private int                     postType;
    private ViewStub                viewStub;


    @Override
    protected View initMainView( Bundle savedInstanceState )
    {
        View v = getLayoutInflater().inflate( R.layout.activity_facebook_detail, null );
        facebookHeader = ( FacebookHeader ) v.findViewById( R.id.facebook_header );
        tvPostText = ( LinkifiedTextView ) v.findViewById( R.id.tv_post_text );
        viewStub = ( ViewStub ) v.findViewById( R.id.facebook_view_stub );
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
        if ( null == getSupportFragmentManager().findFragmentByTag( FacebookActionsFragment.TAG ) )
        {
            facebookActionsFragment = FacebookActionsFragment.newInstance( post, FacebookActionClickedEvent.ACTION_LIKE );
            getSupportFragmentManager()
                    .beginTransaction()
                    .add( getSlidingView().getId(), facebookActionsFragment, FacebookActionsFragment.TAG )
                    .commit();
        }
        else
        {
            facebookActionsFragment = ( FacebookActionsFragment ) getSupportFragmentManager().findFragmentByTag( FacebookActionsFragment.TAG );
        }
        return v;
    }

    @Override
    protected View getDragView()
    {
        return facebookActionsFragment.getViewPagerIndicator();
    }

    private void showMedia( Bundle savedInstancState )
    {
        viewStub.setLayoutResource( R.layout.facebook_detail_activity_type_video );
        FrameLayout temp = ( FrameLayout ) viewStub.inflate();
        final VideoThumbnail videoThumbnail = ( VideoThumbnail ) temp.getChildAt( 0 );
        videoThumbnail.setVisibility( View.GONE );
        final String imageUrl = UrlModifier.getFacebookLargePhotoUrl( post.getPicture() );
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
