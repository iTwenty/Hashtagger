package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterNormalRow extends TwitterListRow
{
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
        super.init( context );
    }

    @Override
    protected LinkifiedTextView initStatusTextView()
    {
        return ( LinkifiedTextView ) findViewById( R.id.tv_status_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.twitter_buttons );
    }

    @Override
    protected SitesHeader initSitesHeader()
    {
        return ( SitesHeader ) findViewById( R.id.twitter_header );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
    }
}
