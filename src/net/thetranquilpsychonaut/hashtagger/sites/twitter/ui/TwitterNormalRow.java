package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import twitter4j.Status;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterNormalRow extends SitesListRow
{
    private TwitterHeader twitterHeader;
    private TextView      tvTweet;
    private Status        status;

    protected TwitterNormalRow( Context context )
    {
        super( context );
    }

    protected TwitterNormalRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected TwitterNormalRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.twitter_normal_row, this );
        twitterHeader = ( TwitterHeader ) findViewById( R.id.twitter_header );
        tvTweet = ( TextView ) findViewById( R.id.tv_tweet );
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
    }
}
