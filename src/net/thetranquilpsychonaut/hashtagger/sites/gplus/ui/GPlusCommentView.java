package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Comment;
import net.thetranquilpsychonaut.hashtagger.sites.ui.CommentView;

/**
 * Created by itwenty on 6/15/14.
 */
public class GPlusCommentView extends CommentView
{
    public GPlusCommentView( Context context )
    {
        super( context );
    }

    public GPlusCommentView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public GPlusCommentView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected String getComment( Object result )
    {
        return ( ( Comment ) result ).getObject().getOriginalContent();
    }

    @Override
    protected String getCommenterName( Object result )
    {
        return ( ( Comment ) result ).getActor().getDisplayName();
    }

    @Override
    protected String getCommenterImageUrl( Object result )
    {
        return ( ( Comment ) result ).getActor().getImage().getUrl();
    }
}
