package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.PersonView;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;

/**
 * Created by itwenty on 6/14/14.
 */
public class FacebookLikeView extends PersonView
{
    public FacebookLikeView( Context context )
    {
        super( context );
    }

    public FacebookLikeView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public FacebookLikeView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected String getPersonImageUrl( Object result )
    {
        return UrlModifier.getFacebookProfilePictureUrl( ( ( Post ) result ).getFrom().getId() );
    }

    @Override
    protected String getPersonName( Object result )
    {
        return ( ( Post ) result ).getFrom().getName();
    }
}
