package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.components.GPlusData;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumThumbnailsFragment;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 5/16/14.
 */
public class GPlusDetailActivity extends SitesDetailActivity
{
    private TextView    tvActivityText;
    private GPlusHeader gPlusHeader;
    private Activity    activity;
    private int         activityType;

    private ViewStub        viewStub;
    private ImageView       imgvPhoto;
    private GPlusDetailView gPlusDetailView;
    private FrameLayout     flPlaceHolder;
    private List<String>    albumThumbnailUrls;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_gplus_detail );
        tvActivityText = ( TextView ) findViewById( R.id.tv_activity_text );
        gPlusHeader = ( GPlusHeader ) findViewById( R.id.gplus_header );
        viewStub = ( ViewStub ) findViewById( R.id.gplus_view_stub );
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
        tvActivityText.setText( Html.fromHtml( activity.getObject().getContent() ) );
        tvActivityText.setMovementMethod( LinkMovementMethod.getInstance() );
        if ( activityType == GPlusListAdapter.ACTIVITY_TYPE_PHOTO )
        {
            showPhoto( savedInstanceState );
        }
        else if ( activityType == GPlusListAdapter.ACTIVITY_TYPE_VIDEO || activityType == GPlusListAdapter.ACTIVITY_TYPE_LINK )
        {
            showDetails( savedInstanceState );
        }
        else if ( activityType == GPlusListAdapter.ACTIVITY_TYPE_ALBUM )
        {
            showAlbumThumbnails( savedInstanceState );
        }
    }

    private void showPhoto( Bundle savedInstanceState )
    {
        viewStub.setLayoutResource( R.layout.gplus_detail_activity_type_photo );
        imgvPhoto = ( ImageView ) viewStub.inflate();
        imgvPhoto.setVisibility( View.GONE );
        final String imageUrl;
        if ( null != activity.getObject().getAttachments().get( 0 ).getFullImage() )
        {
            imageUrl = activity.getObject().getAttachments().get( 0 ).getFullImage().getUrl();
        }
        else
        {
            imageUrl = activity.getObject().getAttachments().get( 0 ).getImage().getUrl();
        }
        Picasso.with( this )
                .load( imageUrl )
                .into( imgvPhoto, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        imgvPhoto.setVisibility( View.VISIBLE );
                    }

                    @Override
                    public void onError()
                    {

                    }
                } );
        imgvPhoto.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                ViewAlbumActivity.createAndStartActivity( v.getContext(), Helper.createStringArrayList( imageUrl ), 0 );
            }
        } );
    }


    private void showDetails( Bundle savedInstanceState )
    {
        viewStub.setLayoutResource( R.layout.gplus_detail_activity_type_video_or_link );
        FrameLayout temp = ( FrameLayout ) viewStub.inflate();
        gPlusDetailView = ( GPlusDetailView ) temp.getChildAt( 0 );
        gPlusDetailView.showDetails( activity );
    }


    private void showAlbumThumbnails( Bundle savedInstanceState )
    {
        viewStub.setLayoutResource( R.layout.gplus_detail_activity_type_album_placeholder );
        flPlaceHolder = ( FrameLayout ) viewStub.inflate();
        if ( getSupportFragmentManager().findFragmentByTag( ViewAlbumThumbnailsFragment.TAG ) == null )
        {
            albumThumbnailUrls = new ArrayList<String>( activity.getObject().getAttachments().get( 0 ).getThumbnails().size() );
            for ( Activity.PlusObject.Attachments.Thumbnails thumbnail : activity.getObject().getAttachments().get( 0 ).getThumbnails() )
            {
                albumThumbnailUrls.add( thumbnail.getImage().getUrl() );
            }
            getSupportFragmentManager().beginTransaction()
                    .add( flPlaceHolder.getId(),
                            ViewAlbumThumbnailsFragment.newInstance( ( ArrayList<String> ) albumThumbnailUrls, true, HashtaggerApp.GPLUS_VALUE ),
                            ViewAlbumThumbnailsFragment.TAG )
                    .commit();
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
        return tvActivityText;
    }
}