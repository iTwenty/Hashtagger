package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumFragment;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.ArrayList;

/**
 * Created by itwenty on 5/16/14.
 */
public class GPlusDetailActivity extends SitesDetailActivity
{
    private TextView       tvContent;
    private GPlusHeader    gPlusHeader;
    private GPlusMediaView gPlusMediaView;
    private Activity       activity;
    private int            activityType;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_gplus_detail );
        tvContent = ( TextView ) findViewById( R.id.tv_content );
        gPlusHeader = ( GPlusHeader ) findViewById( R.id.gplus_header );
        gPlusMediaView = ( GPlusMediaView ) findViewById( R.id.gplus_media_view );
        activity = GPlusData.ActivityData.popActivity();
        if ( null == activity )
        {
            activity = ( Activity ) getLastCustomNonConfigurationInstance();
        }
        if ( null == activity )
        {
            finish();
        }
        activityType = GPlusListAdapter.getActivityType( activity );
        gPlusHeader.showHeader( activity );
        tvContent.setText( Html.fromHtml( activity.getObject().getContent() ) );
        tvContent.setMovementMethod( LinkMovementMethod.getInstance() );

        if ( activityType == GPlusListAdapter.ACTIVITY_TYPE_MEDIA )
        {
            gPlusMediaView.showMedia( this.activity );
            gPlusMediaView.setVisibility( View.VISIBLE );
        }
        if ( activityType == GPlusListAdapter.ACTIVITY_TYPE_ALBUM )
        {
            if ( null == savedInstanceState )
            {
                ArrayList<String> albumThumbnailUrls = ( ArrayList<String> ) activity.get( ViewAlbumFragment.ALBUM_THUMBNAIL_URLS_KEY );
                ArrayList<String> albumImageUrls = new ArrayList<String>( albumThumbnailUrls.size() );
                for ( String url : albumThumbnailUrls )
                {
                    albumImageUrls.add( Helper.getGPlusLargeImageUrl( url ) );
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .add( R.id.fl_placeholder, ViewAlbumFragment.newInstance( albumImageUrls, 0 ), ViewAlbumFragment.TAG )
                        .commit();
            }
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance()
    {
        return this.activity;
    }

    @Override
    protected TextView getLinkedTextView()
    {
        return tvContent;
    }
}