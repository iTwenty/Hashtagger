package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/18/14.
 */
public class FacebookButtons extends LinearLayout
{
    private Button btnLike;
    private Button btnComment;
    private Button btnShare;

    public FacebookButtons( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookButtons( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookButtons( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_buttons, this );
        btnLike = ( Button ) findViewById( R.id.btn_like );
        btnComment = ( Button ) findViewById( R.id.btn_comment );
        btnShare = ( Button ) findViewById( R.id.btn_share );
    }

    public void setLikeClickListener( OnClickListener listener )
    {
        btnLike.setOnClickListener( listener );
    }

    public void setCommentClickListener( OnClickListener listener )
    {
        btnComment.setOnClickListener( listener );
    }

    public void setShareClickListener( OnClickListener listener )
    {
        btnShare.setOnClickListener( listener );
    }

}
