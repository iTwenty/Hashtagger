package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.sites.ui.LoadingActivity;

/**
 * Created by itwenty on 5/5/14.
 */
public class GPlusLoginActivity extends LoadingActivity
{
    @Override
    protected View initMainView( Bundle savedInstanceState )
    {
        TextView tv = new TextView( this );
        tv.setText( "Google+" );
        return tv;
    }

    @Override
    protected void onViewsCreated( Bundle savedInstanceState )
    {
    }
}
