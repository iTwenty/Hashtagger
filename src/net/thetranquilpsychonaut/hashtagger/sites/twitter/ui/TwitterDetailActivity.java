package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterActionsPerformer;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.BaseActivity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewAlbumActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/10/14.
 */
public class TwitterDetailActivity extends BaseActivity implements View.OnClickListener, TwitterActionsPerformer.OnTwitterActionDoneListener
{
    public static final String STATUS_KEY = "status";
    private LinkifiedTextView       tvStatusText;
    private TwitterHeader           twitterHeader;
    private ViewStub                viewStub;
    private ImageButton             imgbReply;
    private ImageButton             imgbRetweet;
    private ImageButton             imgbFavorite;
    private Status                  status;
    private TwitterActionsPerformer twitterActionsPerformer;
    private int                     statusType;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_twitter_detail );
        tvStatusText = ( LinkifiedTextView ) findViewById( R.id.tv_status_text );
        twitterHeader = ( TwitterHeader ) findViewById( R.id.twitter_header );
        viewStub = ( ViewStub ) findViewById( R.id.twitter_view_stub );
        imgbReply = ( ImageButton ) findViewById( R.id.imgb_reply );
        imgbRetweet = ( ImageButton ) findViewById( R.id.imgb_retweet );
        imgbFavorite = ( ImageButton ) findViewById( R.id.imgb_favorite );
        if ( null == getIntent() )
        {
            finish();
        }
        status = ( Status ) getIntent().getSerializableExtra( STATUS_KEY );
        if ( null == status )
        {
            finish();
        }
        this.statusType = TwitterListAdapter.getStatusType( status );
        twitterHeader.showHeader( status );
        tvStatusText.setText( status.getLinkedText() );
        imgbReply.setOnClickListener( this );
        imgbRetweet.setOnClickListener( this );
        imgbFavorite.setOnClickListener( this );
        updateActionsButtons();
        if ( statusType == TwitterListAdapter.STATUS_TYPE_PHOTO )
        {
            showPhoto( savedInstanceState );
        }
    }

    private void updateActionsButtons()
    {
        if ( status.isRetweeted() )
        {
            imgbRetweet.setImageResource( R.drawable.retweet_on );
        }
        else
        {
            imgbRetweet.setImageResource( R.drawable.retweet );
        }
        if ( status.isFavorited() )
        {
            imgbFavorite.setImageResource( R.drawable.favorite_on );
        }
        else
        {
            imgbFavorite.setImageResource( R.drawable.favorite );
        }
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
    protected void onStart()
    {
        super.onStart();
        Helper.debug( "TDA onStart" );
        HashtaggerApp.bus.register( this );
        twitterActionsPerformer = new TwitterActionsPerformer( getSupportFragmentManager() );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Helper.debug( "TDA onStop" );
        HashtaggerApp.bus.unregister( this );
        twitterActionsPerformer = null;
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( imgbReply ) )
        {
            twitterActionsPerformer.doReply( status );
        }
        if ( v.equals( imgbRetweet ) )
        {
            twitterActionsPerformer.doRetweet( status );
        }
        if ( v.equals( imgbFavorite ) )
        {
            twitterActionsPerformer.doFavorite( status );
        }
    }

    @Subscribe
    public void onRetweetDone( TwitterRetweetDoneEvent event )
    {
        this.status = event.getStatus();
        if ( event.getSuccess() )
        {
            updateActionsButtons();
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
        this.status = event.getStatus();
        Helper.debug( "TDA onFavDone" );
        if ( event.getSuccess() )
        {
            Helper.debug( String.valueOf( event.getStatus().isFavorited() ) );
            updateActionsButtons();
        }
        else
        {
            Toast.makeText( this, "Failed to favorite", Toast.LENGTH_SHORT ).show();
        }
    }

    @Subscribe
    public void onReplyDone( TwitterReplyDoneEvent event )
    {
        this.status = event.getStatus();
        if ( event.getSuccess() )
        {
            Toast.makeText( this, "Replied like a champ!", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( this, "Failed to reply", Toast.LENGTH_SHORT ).show();
        }
    }
}
