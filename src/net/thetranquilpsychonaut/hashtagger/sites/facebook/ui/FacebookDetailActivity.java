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

    private FacebookDetailView facebookMediaView = null;

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
    }

    @Override
    protected TextView getLinkedTextView()
    {
        return tvMessage;
    }
}
