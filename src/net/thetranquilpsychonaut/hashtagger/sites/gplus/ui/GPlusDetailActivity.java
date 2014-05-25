package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/16/14.
 */
public class GPlusDetailActivity extends BaseActivity
{
    private TextView tvContent;
    private GPlusHeader gPlusHeader;
    private Activity activity;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_gplus_detail );
        tvContent = ( TextView ) findViewById( R.id.tv_content );
        gPlusHeader = ( GPlusHeader ) findViewById( R.id.gplus_header );
        activity = GPlusData.ActivityData.popActivity();
        if ( null == activity )
        {
            finish();
        }
        gPlusHeader.updateHeader( activity );
        tvContent.setText( Helper.stripUnderlines( new SpannableString( Html.fromHtml( activity.getObject().getContent() ) ) ) );
    }
}