package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.PopupMenu;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/30/14.
 */
public abstract class TwitterListRow extends SitesListRow
{
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
        tvStatusText = initStatusTextView();
    }

    protected abstract LinkifiedTextView initStatusTextView();

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        this.status = ( Status ) result;
        tvStatusText.setText( status.isRetweet() ? status.getRetweetedStatus().getLinkedText() : status.getLinkedText() );
    }

    @Override
    protected int getPopupMenuResId()
    {
        return R.menu.twitter_list_row_popup_menu;
    }

    @Override
    protected boolean onPopupMenuItemClicked( PopupMenu menu, MenuItem item )
    {
        return true;
    }

    @Override
    protected Uri getResultUrl()
    {
        return UrlModifier.getTwitterStatusUrl( status );
    }

    @Override
    protected String getResultText()
    {
        return status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText();
    }
}
