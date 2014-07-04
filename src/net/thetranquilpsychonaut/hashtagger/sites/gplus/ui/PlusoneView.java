package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.PersonView;

/**
 * Created by itwenty on 6/14/14.
 */
public class PlusoneView extends PersonView
{
    public PlusoneView( Context context )
    {
        super( context );
    }

    public PlusoneView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public PlusoneView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected String getPersonImageUrl( Object result )
    {
        return ( ( Activity ) result ).getActor().getImage().getUrl();
    }

    @Override
    protected String getPersonName( Object result )
    {
        return ( ( Activity ) result ).getActor().getDisplayName();
    }
}
