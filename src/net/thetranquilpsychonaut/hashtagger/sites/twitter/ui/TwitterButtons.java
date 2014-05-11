package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.widgets.CenterContentButton;
import twitter4j.Status;

/**
 * Created by itwenty on 4/16/14.
 */
public class TwitterButtons extends SitesButtons implements View.OnClickListener
{
    private CenterContentButton ccbReply;
    private CenterContentButton ccbRetweet;
    private CenterContentButton ccbOpenInBrowser;
    private CenterContentButton ccbViewDetails;
    private Status              status;

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
        ccbReply = ( CenterContentButton ) findViewById( R.id.ccb_reply );
        ccbRetweet = ( CenterContentButton ) findViewById( R.id.ccb_retweet );
        ccbOpenInBrowser = ( CenterContentButton ) findViewById( R.id.ccb_open_in_browser );
        ccbViewDetails = ( CenterContentButton ) findViewById( R.id.ccb_view_details );
    }

    @Override
    protected void updateButtons( Object result )
    {
        this.status = ( Status ) result;
        ccbReply.setOnClickListener( this );
        ccbRetweet.setOnClickListener( this );
        ccbOpenInBrowser.setOnClickListener( this );
        ccbViewDetails.setOnClickListener( this );
        if ( status.getRetweetCount() != 0 )
        {
            ccbRetweet.setText( String.valueOf( status.getRetweetCount() ) );
        }
        setRetweeted( status.isRetweetedByMe() );
    }

    private void setRetweeted( boolean isRetweetedByMe )
    {
        if ( isRetweetedByMe )
        {
            ccbRetweet.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.twitter_retweet_on ), null, null, null );
        }
        else
        {
            ccbRetweet.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable( R.drawable.twitter_retweet ), null, null, null );
        }
    }

    @Override
    protected void clearButtons()
    {
        this.status = null;
        ccbReply.setOnClickListener( null );
        ccbRetweet.setOnClickListener( null );
        ccbOpenInBrowser.setOnClickListener( null );
        ccbViewDetails.setOnClickListener( null );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( ccbReply ) )
        {
            TwitterReplyDialog dialog = TwitterReplyDialog.newInstance( status.getUser().getScreenName(), status.getId() );
            dialog.show( ( ( FragmentActivity ) getContext() ).getFragmentManager(), TwitterReplyDialog.TAG );
        }
        if ( v.equals( ccbRetweet ) )
        {
            TwitterRetweetDialog dialog = TwitterRetweetDialog.newIntance( status.getId() );
            dialog.show( ( ( FragmentActivity ) getContext() ).getFragmentManager(), TwitterRetweetDialog.TAG );
        }
        if ( v.equals( ccbOpenInBrowser ) )
        {
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setData( Uri.parse( "http://twitter.com/" + status.getUser().getId() + "/status/" + status.getId() ) );
            getContext().startActivity( i );
        }
        if ( v.equals( ccbViewDetails ) )
        {
            Intent i = new Intent( getContext(), StatusDetailActivity.class );
            i.putExtra( StatusDetailActivity.STATUS_KEY, status );
            getContext().startActivity( i );
        }
    }
}
