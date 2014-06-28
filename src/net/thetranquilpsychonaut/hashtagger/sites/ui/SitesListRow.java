package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/15/14.
 */
public abstract class SitesListRow extends RelativeLayout implements View.OnClickListener
{
    protected SitesButtons sitesButtons;
    protected SitesHeader  sitesHeader;
    protected ImageView    popupMenuAnchor;

    protected SitesListRow( Context context )
    {
        this( context, null, R.attr.sitesListRowStyle );
    }

    protected SitesListRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, R.attr.sitesListRowStyle );
    }

    protected SitesListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        init( context );
        sitesButtons = initSitesButtons();
        sitesHeader = initSitesHeader();
        popupMenuAnchor = initPopupMenuAnchor();
        if ( null == sitesButtons || null == sitesHeader || null == popupMenuAnchor )
        {
            throw new RuntimeException( "SitesButtons, SitesHeader or PopupMenuAnchor must not be null!" );
        }
        popupMenuAnchor.setOnClickListener( this );
    }

    protected abstract void init( Context context );

    protected abstract SitesButtons initSitesButtons();

    protected abstract SitesHeader initSitesHeader();

    protected abstract ImageView initPopupMenuAnchor();

    public void expandRow( final Object result, boolean animate, AnimatorListenerAdapter adapter )
    {
        sitesButtons.show( result, animate, adapter );
    }

    public void updateRow( final Object result )
    {
        sitesHeader.updateHeader( result );
    }

    public void collapseRow( boolean animate )
    {
        sitesButtons.hide( animate );
    }

    public boolean isExpanded()
    {
        return sitesButtons.mIsVisible;
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( popupMenuAnchor ) )
        {
            final PopupMenu menu = new PopupMenu( getContext(), v );
            menu.inflate( getPopupMenuResId() );
            menu.inflate( R.menu.sites_list_row_popup_menu );
            menu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick( MenuItem item )
                {
                    if ( item.equals( menu.getMenu().findItem( R.id.it_open_in_browser ) ) )
                    {
                        Intent i = new Intent( Intent.ACTION_VIEW );
                        i.setData( getResultUrl() );
                        getContext().startActivity( i );
                        return true;
                    }
                    else if ( item.equals( menu.getMenu().findItem( R.id.it_share ) ) )
                    {
                        Intent i = new Intent( Intent.ACTION_SEND );
                        i.putExtra( Intent.EXTRA_TEXT, getResultText() );
                        i.setType( "text/plain" );
                        getContext().startActivity( i );
                        return true;
                    }
                    else
                    {
                        return onPopupMenuItemClicked( menu, item );
                    }
                }
            } );
            menu.show();
        }
    }

    protected abstract boolean onPopupMenuItemClicked( PopupMenu menu, MenuItem item );

    protected abstract int getPopupMenuResId();

    protected abstract Uri getResultUrl();

    protected abstract String getResultText();
}
