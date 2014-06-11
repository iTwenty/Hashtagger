package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/30/14.
 */
public abstract class TwitterListRow extends SitesListRow
{
    protected TwitterHeader     twitterHeader;
    protected LinkifiedTextView tvStatusText;
    protected Status            status;

    protected TwitterListRow( Context context )
    {
        super( context );
    }

    protected TwitterListRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected TwitterListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        twitterHeader = initTwitterHeader();
        tvStatusText = initStatusText();
        super.init( context );
    }

    protected abstract TwitterHeader initTwitterHeader();

    protected abstract LinkifiedTextView initStatusText();

    @Override
    public void updateRow( Object result )
    {
        this.status = ( Status ) result;
        twitterHeader.showHeader( status );
        tvStatusText.setText( status.isRetweet() ? status.getRetweetedStatus().getLinkedText() : status.getLinkedText() );
    }
}
