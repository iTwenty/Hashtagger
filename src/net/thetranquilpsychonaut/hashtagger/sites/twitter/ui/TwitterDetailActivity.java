package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.Actions;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SlidingUpPanelDetailActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/10/14.
 */
public class TwitterDetailActivity extends SlidingUpPanelDetailActivity
{
    public static final String STATUS_KEY = "status";

    private LinkifiedTextView tvStatusText;
    private TwitterHeader     twitterHeader;
    private ViewStub          viewStub;
    private int               statusType;

    // Passing status via Intent.putExtra() seems to pass a new copy of the status
    // rather than reference to same status. So we pass the status to this activity
    // statically and make sure to null it out in onStop(). We still need to preserve
    // the status across screen rotation, so we persist it in the bundle in
    // onSaveInstanceState() which is guaranteed to be called before onStop().
    private static Status status;

    public static void createAndStartActivity( Status status, Context context )
    {
        TwitterDetailActivity.status = status;
        Intent i = new Intent( context, TwitterDetailActivity.class );
        context.startActivity( i );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        SitesButtons buttons = ( SitesButtons ) LayoutInflater.from( this ).inflate( R.layout.activity_twitter_detail_buttons, null );
        buttons.updateButtons( status );
        return buttons;
    }

    @Override
    protected View initMainView( Bundle savedInstanceState )
    {
        View v = LayoutInflater.from( this ).inflate( R.layout.activity_twitter_detail, null );
        tvStatusText = ( LinkifiedTextView ) v.findViewById( R.id.tv_status_text );
        twitterHeader = ( TwitterHeader ) v.findViewById( R.id.twitter_header );
        viewStub = ( ViewStub ) v.findViewById( R.id.twitter_view_stub );
        if ( null != savedInstanceState )
        {
            status = ( Status ) savedInstanceState.getSerializable( STATUS_KEY );
        }
        // Should never happen
        if ( null == status )
        {
            finish();
        }
        setTitle( "@" + status.getUser().getScreenName() + "'s tweet" );
        this.statusType = TwitterListAdapter.getStatusType( status );
        twitterHeader.updateHeader( status );
        tvStatusText.setText( status.isRetweet() ? status.getRetweetedStatus().getLinkedText() : status.getLinkedText() );
        if ( statusType == TwitterListAdapter.STATUS_TYPE_PHOTO )
        {
            showPhoto( savedInstanceState );
        }
        return v;
    }

    @Override
    protected String getSitesActionsFragmentTag()
    {
        return TwitterActionsFragment.TAG;
    }

    @Override
    protected Fragment initSitesActionsFragment()
    {
        return TwitterActionsFragment.newInstance( status.isRetweet() ? status.getRetweetedStatus() : status, Actions.ACTION_TWITTER_RETWEET );
    }

    private void showPhoto( Bundle savedInstanceState )
    {
        viewStub.setLayoutResource( R.layout.twitter_detail_activity_type_photo );
        final ImageView imgvPhoto = ( ImageView ) viewStub.inflate();
        imgvPhoto.setVisibility( View.GONE );
        final String imageUrl = UrlModifier.getTwitterLargePhotoUrl( status.getEntities().getMedia().get( 0 ).getMediaUrl() );
        Helper.debug( imageUrl );
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
                ViewAlbumActivity.createAndStartActivity( v.getContext(), "@" + status.getUser().getScreenName(), Helper.createStringArrayList( imageUrl ), 0 );
            }
        } );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        status = null;
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putSerializable( STATUS_KEY, status );
    }

    @Subscribe
    public void onRetweetDone( TwitterRetweetDoneEvent event )
    {
        if ( event.isSuccess() )
        {
            sitesButtons.updateButtons( status );
            Toast.makeText( this, "Retweeted like a champ!", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( this, "Failed to retweet", Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void onFavoriteDone( TwitterFavoriteDoneEvent event )
    {
        Helper.debug( event.getStatus() == status ? "equal" : "unequal" );
        if ( event.isSuccess() )
        {
            sitesButtons.updateButtons( status );
        }
        else
        {
            Toast.makeText( this, "Failed to favorite", Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void onReplyDone( TwitterReplyDoneEvent event )
    {
        if ( event.isSuccess() )
        {
            Toast.makeText( this, "Replied like a champ!", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( this, "Failed to reply", Toast.LENGTH_SHORT ).show();
        }
    }
}
