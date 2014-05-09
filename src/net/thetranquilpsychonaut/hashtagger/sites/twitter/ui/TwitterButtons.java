package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import twitter4j.Status;

/**
 * Created by itwenty on 4/16/14.
 */
public class TwitterButtons extends SitesButtons implements View.OnClickListener
{
    private Button btnReply;
    private Button btnRetweet;
    private Button btnFavorite;
    private Button btnViewDetails;
    private Status status;

    public TwitterButtons( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterButtons( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterButtons( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.twitter_buttons, this );
        btnReply = ( Button ) findViewById( R.id.imgb_reply );
        btnRetweet = ( Button ) findViewById( R.id.imgb_retweet );
        btnFavorite = ( Button ) findViewById( R.id.imgb_favorite );
        btnViewDetails = ( Button ) findViewById( R.id.btn_view_details );
    }

    @Override
    protected void updateButtons( Object result )
    {
        this.status = ( Status ) result;
        btnReply.setOnClickListener( this );
        btnRetweet.setOnClickListener( this );
        btnFavorite.setOnClickListener( this );
        btnViewDetails.setOnClickListener( this );
        if ( status.getRetweetCount() != 0 )
        {
            btnRetweet.setText( String.valueOf( status.getRetweetCount() ) );
        }
        if ( status.getFavoriteCount() != 0 )
        {
            btnFavorite.setText( String.valueOf( status.getFavoriteCount() ) );
        }
        setFavorited( status.isFavorited() );
        setRetweeted( status.isRetweetedByMe() );
    }

    private void setRetweeted( boolean isRetweetedByMe )
    {
        if ( isRetweetedByMe )
        {
            btnRetweet.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.twitter_retweet_on ), null, null, null );
        }
        else
        {
            btnRetweet.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.twitter_retweet ), null, null, null );
        }
    }

    private void setFavorited( boolean isFavorited )
    {
        if ( isFavorited )
        {
            btnFavorite.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.twitter_favorite_on ), null, null, null );
        }
        else
        {
            btnFavorite.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.twitter_favorite ), null, null, null );
        }
    }

    @Override
    protected void clearButtons()
    {
        this.status = null;
        btnReply.setOnClickListener( null );
        btnRetweet.setOnClickListener( null );
        btnFavorite.setOnClickListener( null );
        btnViewDetails.setOnClickListener( null );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( btnReply ) )
        {
            Helper.debug( "Reply clicked " + status.getUser().getName() );
        }
        if ( v.equals( btnRetweet ) )
        {
            Helper.debug( "Rwtweet clicked " + status.getUser().getName() );
        }
        if ( v.equals( btnFavorite ) )
        {
            Helper.debug( "Favorite clicked " + status.getUser().getName() );
        }
        if ( v.equals( btnViewDetails ) )
        {
            Helper.debug( "Details clicked " + status.getUser().getName() );
        }
    }
}
