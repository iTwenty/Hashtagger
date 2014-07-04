package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Comment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.CommentView;
import net.thetranquilpsychonaut.hashtagger.utils.UrlModifier;

/**
 * Created by itwenty on 6/14/14.
 */
public class FacebookCommentView extends CommentView
{
    public FacebookCommentView( Context context )
    {
        super( context );
    }

    public FacebookCommentView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public FacebookCommentView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected String getCommenterImageUrl( Object result )
    {
        return UrlModifier.getFacebookProfilePictureUrl( ( ( Comment ) result ).getFrom().getId() );
    }

    @Override
    protected String getCommenterName( Object result )
    {
        return ( ( Comment ) result ).getFrom().getName();
    }

    @Override
    protected String getComment( Object result )
    {
        return ( ( Comment ) result ).getMessage();
    }
}
