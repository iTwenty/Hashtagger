package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.os.Bundle;
import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SavedHashtagsActivity;

/**
 * Created by itwenty on 5/16/14.
 */
public class GPlusDetailActivity extends SavedHashtagsActivity
{
    private TextView tvContent;
    private Activity activity;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        RelativeLayout rl = ( RelativeLayout ) getLayoutInflater().inflate( R.layout.activity_gplus_detail, null, false );
        dlNavDrawer.addView( rl, 0 );
        tvContent = ( TextView ) findViewById( R.id.tv_content );
        activity = GPlusData.ActivityData.popActivity();
        tvContent.setText( Html.fromHtml( activity.getObject().getContent() ) );
    }
}