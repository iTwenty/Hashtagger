package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/18/14.
 */
public class FacebookDetailActivity extends SitesDetailActivity
{
    public static final String POST_KEY = "post";

    private FacebookHeader facebookHeader;
    private TextView       tvPostText;
    private Post           post;
    private int            postType;

    private ViewStub           viewStub;
    private ImageView          imgvPhoto;
    private FacebookDetailView facebookDetailView;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_facebook_detail );
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvPostText = ( TextView ) findViewById( R.id.tv_post_text );
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
        tvPostText.setText( post.getMessage() );
        tvPostText.setMovementMethod( LinkMovementMethod.getInstance() );
        if ( postType == FacebookListAdapter.POST_TYPE_PHOTO )
        {
            showPhoto( savedInstanceState );
        }
        if ( postType == FacebookListAdapter.POST_TYPE_VIDEO || postType == FacebookListAdapter.POST_TYPE_LINK )
        {
            showDetails( savedInstanceState );
        }
    }

    private void showPhoto( Bundle savedInstanceState )
    {
        viewStub.setLayoutResource( R.layout.facebook_detail_activity_type_photo );
        imgvPhoto = ( ImageView ) viewStub.inflate();
        imgvPhoto.setVisibility( View.GONE );
        final String imageUrl = post.getPicture().toString();
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
                ViewAlbumActivity.createAndStartActivity( v.getContext(), post.getName(), Helper.createStringArrayList( imageUrl ), 0 );
            }
        } );
    }

    private void showDetails( Bundle savedInstancState )
    {
        viewStub.setLayoutResource( R.layout.facebook_detail_activity_type_video_or_link );
        FrameLayout temp = ( FrameLayout ) viewStub.inflate();
        facebookDetailView = ( FacebookDetailView ) temp.getChildAt( 0 );
        facebookDetailView.showDetails( post );
    }

    @Override
    protected TextView getLinkedTextView()
    {
        return tvPostText;
    }
}
