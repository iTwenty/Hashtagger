package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/18/14.
 */
public class FacebookDetailActivity extends SitesDetailActivity implements View.OnClickListener
{
    public static final String POST_KEY = "post";

    private FacebookHeader facebookHeader;
    private TextView       tvMessage;
    private Post           post;
    private ImageView      imagvMediaImage;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_facebook_detail );
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        imagvMediaImage = ( ImageView ) findViewById( R.id.imgv_media_image );
        this.post = ( Post ) getIntent().getSerializableExtra( POST_KEY );
        facebookHeader.updateHeader( post );
        tvMessage.setText( post.getMessage() );
        Helper.linkifyFacebook( tvMessage );
        tvMessage.setMovementMethod( LinkMovementMethod.getInstance() );
        imagvMediaImage.setOnClickListener( this );
        if ( FacebookListAdapter.getPostType( this.post ) == FacebookListAdapter.POST_TYPE_MEDIA )
        {
            Picasso.with( this )
                    .load( post.getPicture().toString().replace( "_s", "_o" ) )
                    .into( imagvMediaImage, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            if ( "video".equals( post.getType() ) )
                            {
                                findViewById( R.id.imgv_play ).setVisibility( View.VISIBLE );
                            }
                            findViewById( R.id.fl_wrapper ).setVisibility( View.VISIBLE );
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
        return tvMessage;
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imagvMediaImage ) )
        {
            if ( "video".equals( post.getType() ) )
            {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( post.getSource().toString() ) );
                startActivity( intent );
            }
            else if ( "photo".equals( post.getType() ) )
            {
                Intent intent = new Intent( this, ViewImageActivity.class );
                intent.putExtra( ViewImageActivity.IMAGE_URL_KEY, post.getPicture().toString().replace( "_s.", "_o." ) );
                startActivity( intent );
            }
        }
    }
}
