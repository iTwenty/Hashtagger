package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.os.Bundle;
import android.widget.TextView;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;

/**
 * Created by itwenty on 5/18/14.
 */
public class FacebookDetailActivity extends BaseActivity
{
    public static final String POST_KEY = "post";

    private FacebookHeader facebookHeader;
    private TextView       tvMessage;
    private Post           post;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_facebook_detail );
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        this.post = ( Post ) getIntent().getSerializableExtra( POST_KEY );
        facebookHeader.updateHeader( post );
        tvMessage.setText( post.getMessage() );
    }
}
