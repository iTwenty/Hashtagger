package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;

/**
 * Created by itwenty on 5/7/14.
 */
public class GPlusButtons extends SitesButtons
{
    private Button btnPlusOne;
    private Button btnComment;
    private Button btnShare;

    public GPlusButtons( Context context )
    {
        this( context, null, 0 );
    }

    public GPlusButtons( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public GPlusButtons( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.gplus_buttons, this );
        btnPlusOne = ( Button ) findViewById( R.id.btn_plusone );
        btnComment = ( Button ) findViewById( R.id.btn_comment );
        btnShare = ( Button ) findViewById( R.id.btn_share );
    }

    public void setPlusOneClickListener( OnClickListener listener )
    {
        btnPlusOne.setOnClickListener( listener );
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
