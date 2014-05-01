package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/16/14.
 */
public class TwitterButtons extends LinearLayout
{
    private Button btnReply;
    private Button btnRetweet;
    private Button btnFavorite;

    public TwitterButtons( Context context )
    {
        this( context, null, 0 );
    }

    public TwitterButtons( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public TwitterButtons( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.twitter_buttons, this );
        btnReply = ( Button ) findViewById( R.id.imgb_reply );
        btnRetweet = ( Button ) findViewById( R.id.imgb_retweet );
        btnFavorite = ( Button ) findViewById( R.id.imgb_favorite );
    }

    public void setReplyClickListener( OnClickListener listener )
    {
        btnReply.setOnClickListener( listener );
    }

    public void setRetweetClickListener( OnClickListener listener )
    {
        btnRetweet.setOnClickListener( listener );
    }

    public void setFavoriteClickListener( OnClickListener listener )
    {
        btnFavorite.setOnClickListener( listener );
    }
}
