package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/30/14.
 */
public abstract class GPlusListRow extends SitesListRow
{
    protected TextView    tvActivityText;
    protected Activity    activity;

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
}
