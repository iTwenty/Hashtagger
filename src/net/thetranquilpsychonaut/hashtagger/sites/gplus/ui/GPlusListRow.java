package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.enums.Actions;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/30/14.
 */
public abstract class GPlusListRow extends SitesListRow
{
    protected TextView tvActivityText;
    protected Activity activity;

    protected GPlusListRow( Context context )
    {
        super( context );
    }

    protected GPlusListRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected GPlusListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        tvActivityText = initActivityText();
    }

    protected abstract LinkifiedTextView initActivityText();

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        this.activity = ( Activity ) result;
        tvActivityText.setText( activity.getObject().getLinkedText() );
    }

    @Override
    protected int getPopupMenuResId()
    {
        return R.menu.gplus_list_row_popup_menu;
    }

    @Override
    protected boolean onPopupMenuItemClicked( PopupMenu menu, MenuItem item )
    {
        int actionType;
        Menu itemsMenu = menu.getMenu();
        if ( item.equals( itemsMenu.findItem( R.id.it_view_plusones ) ) )
        {
            actionType = Actions.ACTION_GPLUS_ONE;
        }
        else if ( item.equals( itemsMenu.findItem( R.id.it_view_comments ) ) )
        {
            actionType = Actions.ACTION_GPLUS_COMMENT;
        }
        else if ( item.equals( itemsMenu.findItem( R.id.it_view_reshares ) ) )
        {
            actionType = Actions.ACTION_GPLUS_RESHARE;
        }
        else
        {
            return false;
        }
        GPlusActionsFragment fragment = GPlusActionsFragment.newInstance( activity, actionType );
        fragment.show( ( ( FragmentActivity ) getContext() ).getSupportFragmentManager(), GPlusActionsFragment.TAG );
        return true;
    }

    @Override
    protected Uri getResultUrl()
    {
        return UrlModifier.getGPlusActivityUrl( activity );
    }

    @Override
    protected String getResultText()
    {
        return activity.getObject().getContent();
    }
}
