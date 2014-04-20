package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ViewAnimator;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.ViewExpander;
import twitter4j.Status;

/**
 * Created by itwenty on 4/17/14.
 */
public class TwitterExpandView extends RelativeLayout
{
    private ViewAnimator     vaTwitterExpandView;
    private TwitterLinkView  twitterLinkView;
    private TwitterMediaView twitterMediaView;
    private TwitterButtons   twitterButtons;
    private ViewExpander     expander;

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
        expander = new ViewExpander( this );
    }


    public void expandStatus( Status status, boolean animate )
    {
        boolean hasMedia = status.getMediaEntities().length > 0;
        boolean hasLink = status.getURLEntities().length > 0;
        if ( hasMedia )
        {
            vaTwitterExpandView.setVisibility( View.VISIBLE );
            vaTwitterExpandView.setDisplayedChild( 1 );
            twitterMediaView.showMediaFromStatus( status );
        }
        else if ( hasLink )
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
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec( LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY );
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec( LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY );
        measure( widthMeasureSpec, heightMeasureSpec );
        expander.expandView( getMeasuredHeight(), animate );
    }

    public void collapseStatus( boolean animate )
    {
        expander.collapseView( animate );
    }
}
