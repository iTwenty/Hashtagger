package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

/**
 * Created by itwenty on 5/30/14.
 */
public abstract class GPlusListRow extends SitesListRow
{
    protected GPlusHeader gPlusHeader;
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
        gPlusHeader = initGPlusHeader();
        tvActivityText = initActivityText();
        super.init( context );
    }

    protected abstract GPlusHeader initGPlusHeader();

    protected abstract TextView initActivityText();

    @Override
    public void updateRow( Object result )
    {
        this.activity = ( Activity ) result;
        gPlusHeader.showHeader( activity );
        tvActivityText.setText( activity.getObject().getOriginalContent() );
    }
}
