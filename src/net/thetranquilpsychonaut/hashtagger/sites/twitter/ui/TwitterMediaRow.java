package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;
import twitter4j.Status;

/**
 * Created by itwenty on 5/1/14.
 */
public class TwitterMediaRow extends SitesListRow
{
    TextView tvMedia;

    protected TwitterMediaRow( Context context )
    {
        this( context, null, 0 );
    }

    protected TwitterMediaRow( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    protected TwitterMediaRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.twitter_media_row, this );
        tvMedia = ( TextView ) findViewById( R.id.tv_media );
    }

    @Override
    public void updateRow( Object result )
    {
        Status status = ( Status ) result;
        tvMedia.setText( status.getMediaEntities()[0].getExpandedURL() );
    }
}
