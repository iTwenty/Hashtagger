package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesHeader;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookNormalRow extends FacebookListRow
{
    protected FacebookNormalRow( Context context )
    {
        super( context );
    }

    protected FacebookNormalRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookNormalRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_normal_row, this );
        super.init( context );
    }

    @Override
    protected LinkifiedTextView initPostText()
    {
        return ( LinkifiedTextView ) findViewById( R.id.tv_post_text );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.facebook_buttons );
    }

    @Override
    protected SitesHeader initSitesHeader()
    {
        return ( SitesHeader ) findViewById( R.id.facebook_header );
    }

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
    }
}
