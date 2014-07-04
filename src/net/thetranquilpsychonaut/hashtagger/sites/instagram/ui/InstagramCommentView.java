package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Comment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.CommentView;

/**
 * Created by itwenty on 7/4/14.
 */
public class InstagramCommentView extends CommentView
{
    public InstagramCommentView( Context context )
    {
        super( context );
    }

    public InstagramCommentView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public InstagramCommentView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected String getCommenterImageUrl( Object result )
    {
        return ( ( Comment ) result ).getFrom().getProfilePicture();
    }

    @Override
    protected String getCommenterName( Object result )
    {
        return ( ( Comment ) result ).getFrom().getUserName();
    }

    @Override
    protected String getComment( Object result )
    {
        return ( ( Comment ) result ).getText();
    }
}
