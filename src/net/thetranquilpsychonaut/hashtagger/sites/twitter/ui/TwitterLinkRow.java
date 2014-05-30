package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import twitter4j.Status;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterLinkRow extends SitesListRow implements View.OnClickListener
{
    private TwitterHeader twitterHeader;
    private TextView      tvTweet;
    private TextView      tvTwitterLink;
    private Status        status;

    protected TwitterLinkRow( Context context )
    {
        super( context );
    }

    protected TwitterLinkRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected TwitterLinkRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.twitter_link_row, this );
        twitterHeader = ( TwitterHeader ) findViewById( R.id.twitter_header );
        tvTweet = ( TextView ) findViewById( R.id.tv_tweet );
        tvTwitterLink = ( TextView ) findViewById( R.id.tv_link );
        tvTwitterLink.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.twitter_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        this.status = ( Status ) result;
        twitterHeader.showHeader( status );
        tvTweet.setText( status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText() );
        tvTwitterLink.setText( status.getURLEntities()[0].getExpandedURL() );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( tvTwitterLink ) )
        {
            Intent intent = new Intent( Intent.ACTION_VIEW );
            intent.setData( Uri.parse( status.getURLEntities()[0].getExpandedURL() ) );
            getContext().startActivity( intent );

        }
    }
}
