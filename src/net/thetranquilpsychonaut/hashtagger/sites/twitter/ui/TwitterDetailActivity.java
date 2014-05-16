package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SavedHashtagsActivity;
import twitter4j.Status;

/**
 * Created by itwenty on 5/10/14.
 */
public class TwitterDetailActivity extends SavedHashtagsActivity
{
    public static final String STATUS_KEY = "status";
    private Status   status;
    private TextView tvStatus;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        RelativeLayout rl = ( RelativeLayout ) getLayoutInflater().inflate( R.layout.activity_twitter_detail, null, false );
        dlNavDrawer.addView( rl, 0 );
        tvStatus = ( TextView ) findViewById( R.id.tv_status );
        status = ( Status ) getIntent().getSerializableExtra( STATUS_KEY );
        tvStatus.setText( status.getText() );
    }
}
