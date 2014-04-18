package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ViewAnimator;
import net.thetranquilpsychonaut.hashtagger.R;
import twitter4j.Status;

/**
 * Created by itwenty on 4/17/14.
 */
public class TwitterExpandView extends RelativeLayout
{
    ViewAnimator     vaTwitterExpandView;
    TwitterLinkView  twitterLinkView;
    TwitterMediaView twitterMediaView;
    TwitterButtons   twitterButtons;

    public TwitterExpandView( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterExpandView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterExpandView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        LayoutInflater.from( context ).inflate( R.layout.twitter_expand_view, this, true );
        vaTwitterExpandView = ( ViewAnimator ) findViewById( R.id.va_twitter_expand_view );
        twitterLinkView = ( TwitterLinkView ) findViewById( R.id.twitter_link_view );
        twitterMediaView = ( TwitterMediaView ) findViewById( R.id.twitter_media_view );
        twitterButtons = ( TwitterButtons ) findViewById( R.id.twitter_buttons );
        vaTwitterExpandView.setVisibility( GONE );
    }

    public void showStatus( Status status )
    {
        boolean hasMedia = status.getMediaEntities().length > 0;
        boolean hasLink = status.getURLEntities().length > 0;
        if ( hasMedia )
        {
            vaTwitterExpandView.setVisibility( View.VISIBLE );
            vaTwitterExpandView.setDisplayedChild( 1 );
            twitterMediaView.showMediaFromStatus( status );
        }
        else if( hasLink )
        {
            vaTwitterExpandView.setVisibility( View.VISIBLE );
            vaTwitterExpandView.setDisplayedChild( 0 );
            twitterLinkView.showLinkFromStatus( status );
        }
        else
        {
            vaTwitterExpandView.setVisibility( GONE );
        }
        twitterButtons.setVisibility( VISIBLE );
    }
}
