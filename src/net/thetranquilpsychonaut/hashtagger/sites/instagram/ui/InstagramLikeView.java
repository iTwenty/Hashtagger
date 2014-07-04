package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.From;
import net.thetranquilpsychonaut.hashtagger.sites.ui.PersonView;

/**
 * Created by itwenty on 7/4/14.
 */
public class InstagramLikeView extends PersonView
{
    public InstagramLikeView( Context context )
    {
        super( context );
    }

    public InstagramLikeView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public InstagramLikeView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected String getPersonImageUrl( Object result )
    {
        return ( ( From ) result ).getProfilePicture();
    }

    @Override
    protected String getPersonName( Object result )
    {
        return ( ( From ) result ).getUserName();
    }
}
