package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import net.thetranquilpsychonaut.hashtagger.widgets.LinkifiedTextView;

/**
 * Created by itwenty on 5/31/14.
 */
public abstract class FacebookListRow extends SitesListRow
{
    protected TextView       tvPostText;
    protected Post           post;

    protected FacebookListRow( Context context )
    {
        super( context );
    }

    protected FacebookListRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookListRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        tvPostText = initPostText();
    }

    protected abstract LinkifiedTextView initPostText();

    @Override
    public void updateRow( Object result )
    {
        super.updateRow( result );
        this.post = ( Post ) result;
        tvPostText.setText( post.getLinkedText() );
    }
}
