package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.os.Bundle;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import twitter4j.Status;

/**
 * Created by itwenty on 5/10/14.
 */
public class TwitterDetailActivity extends BaseActivity
{
    public static final String STATUS_KEY = "status";
    private Status   status;
    private TextView tvStatus;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_twitter_detail );
        tvStatus = ( TextView ) findViewById( R.id.tv_status );
        status = ( Status ) getIntent().getSerializableExtra( STATUS_KEY );
        tvStatus.setText( status.getText() );
    }
}
