package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.Actions;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Thumbnail;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesActionsFragment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SlidingUpPanelDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumThumbnailsFragment;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 5/16/14.
 */
public class GPlusDetailActivity extends SlidingUpPanelDetailActivity
{
    public static final String ACTIVITY_KEY = "activity";
    private LinkifiedTextView tvActivityText;
    private GPlusHeader       gPlusHeader;
    private ViewStub          viewStub;
    private Activity          activity;
    private int               activityType;

    public static void createAndStartActivity( Activity activity, Context context )
    {
        Intent i = new Intent( context, GPlusDetailActivity.class );
        i.putExtra( ACTIVITY_KEY, activity );
        context.startActivity( i );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        GPlusButtons buttons = ( GPlusButtons ) LayoutInflater.from( this ).inflate( R.layout.activity_gplus_detail_buttons, null );
        buttons.updateButtons( activity );
        return buttons;
    }

    @Override
    protected View initMainView( Bundle savedInstanceState )
    {
        View v = LayoutInflater.from( this ).inflate( R.layout.activity_gplus_detail, null );
        tvActivityText = ( LinkifiedTextView ) v.findViewById( R.id.tv_activity_text );
        gPlusHeader = ( GPlusHeader ) v.findViewById( R.id.gplus_header );
        viewStub = ( ViewStub ) v.findViewById( R.id.gplus_view_stub );
        if ( null == getIntent() )
        {
            finish();
        }
        activity = ( Activity ) getIntent().getSerializableExtra( ACTIVITY_KEY );
        if ( null == activity )
        {
            finish();
        }
        setTitle( activity.getActor().getDisplayName() + "'s post" );
        activityType = GPlusListAdapter.getActivityType( activity );
        gPlusHeader.updateHeader( activity );
        tvActivityText.setText( activity.getObject().getLinkedText() );
        if ( activityType == GPlusListAdapter.ACTIVITY_TYPE_PHOTO )
        {
            showPhoto( savedInstanceState );
        }
        else if ( activityType == GPlusListAdapter.ACTIVITY_TYPE_VIDEO ||
                activityType == GPlusListAdapter.ACTIVITY_TYPE_LINK )
        {
            showDetails( savedInstanceState );
        }
        else if ( activityType == GPlusListAdapter.ACTIVITY_TYPE_ALBUM )
        {
            showAlbumThumbnails( savedInstanceState );
        }
        return v;
    }

    @Override
    protected String getSitesActionsFragmentTag()
    {
        return GPlusActionsFragment.TAG;
    }

    @Override
    protected SitesActionsFragment initSitesActionsFragment()
    {
        return GPlusActionsFragment.newInstance( activity, Actions.ACTION_GPLUS_ONE );
    }

    private void showPhoto( Bundle savedInstanceState )
    {
        viewStub.setLayoutResource( R.layout.gplus_detail_activity_type_photo );
        final ImageView imgvPhoto = ( ImageView ) viewStub.inflate();
        imgvPhoto.setVisibility( View.GONE );
        final String imageUrl;
        if ( !TextUtils.isEmpty( activity.getObject().getAttachments().get( 0 ).getFullImage().getUrl() ) )
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
                ViewAlbumActivity.createAndStartActivity(
                        v.getContext(),
                        activity.getActor().getDisplayName(),
                        Helper.createStringArrayList( imageUrl ),
                        0 );
            }
        } );
    }


    private void showDetails( Bundle savedInstanceState )
    {
        viewStub.setLayoutResource( R.layout.gplus_detail_activity_type_video_or_link );
        FrameLayout temp = ( FrameLayout ) viewStub.inflate();
        GPlusDetailView gPlusDetailView = ( GPlusDetailView ) temp.getChildAt( 0 );
        gPlusDetailView.showDetails( activity );
    }


    private void showAlbumThumbnails( Bundle savedInstanceState )
    {
        viewStub.setLayoutResource( R.layout.gplus_detail_activity_type_album_placeholder );
        FrameLayout flPlaceHolder = ( FrameLayout ) viewStub.inflate();
        if ( getSupportFragmentManager().findFragmentByTag( ViewAlbumThumbnailsFragment.TAG ) == null )
        {
            List<String> albumThumbnailUrls = new ArrayList<String>(
                    activity.getObject().getAttachments().get( 0 ).getThumbnails().size() );

            for ( Thumbnail thumbnail : activity.getObject().getAttachments().get( 0 ).getThumbnails() )
            {
                albumThumbnailUrls.add( thumbnail.getImage().getUrl() );
            }

            ViewAlbumThumbnailsFragment fragment = ViewAlbumThumbnailsFragment.newInstance(
                    activity.getActor().getDisplayName(),
                    ( ArrayList<String> ) albumThumbnailUrls,
                    true,
                    HashtaggerApp.GPLUS_VALUE );

            getSupportFragmentManager().beginTransaction()
                    .add( flPlaceHolder.getId(),
                            fragment,
                            ViewAlbumThumbnailsFragment.TAG )
                    .commit();
        }
    }
}