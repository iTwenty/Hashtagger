package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.PersonView;

/**
 * Created by itwenty on 7/3/14.
 */
public class TwitterRetweetView extends PersonView
{
    public TwitterRetweetView( Context context )
    {
        super( context );
    }

    public TwitterRetweetView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public TwitterRetweetView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected String getPersonImageUrl( Object result )
    {
        return ( ( Status ) result ).getUser().getProfileImageUrl();
    }

    @Override
    protected String getPersonName( Object result )
    {
        return ( ( Status ) result ).getUser().getName();
    }
}
