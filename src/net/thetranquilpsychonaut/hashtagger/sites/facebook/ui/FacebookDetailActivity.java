package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.RelativeLayout;
import android.widget.TextView;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesDetailActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/18/14.
 */
public class FacebookDetailActivity extends SitesDetailActivity
{
    public static final String POST_KEY = "post";

    private RelativeLayout rlRoot;
    private FacebookHeader facebookHeader;
    private TextView       tvMessage;
    private Post           post;
    private int            postType;

    private FacebookMediaView facebookMediaView = null;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_facebook_detail );
        rlRoot = ( RelativeLayout ) findViewById( R.id.rl_root );
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvMessage = ( TextView ) findViewById( R.id.tv_post_text );
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
        tvMessage.setText( post.getMessage() );
        Helper.linkifyFacebook( tvMessage );
        tvMessage.setMovementMethod( LinkMovementMethod.getInstance() );
        if ( postType == FacebookListAdapter.POST_TYPE_MEDIA )
        {
            facebookMediaView = new FacebookMediaView( this );
            facebookMediaView.setId( R.id.FacebookMediaView_Detail );
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
            params.addRule( RelativeLayout.BELOW, R.id.tv_post_text );
            params.addRule( RelativeLayout.CENTER_HORIZONTAL );
            rlRoot.addView( facebookMediaView, params );
            facebookMediaView.showMedia( this.post );
        }
    }

    @Override
    protected TextView getLinkedTextView()
    {
        return tvMessage;
    }
}
