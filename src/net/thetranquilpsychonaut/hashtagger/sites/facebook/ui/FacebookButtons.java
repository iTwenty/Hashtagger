package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;

/**
 * Created by itwenty on 4/18/14.
 */
public class FacebookButtons extends SitesButtons
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

    @Override
    protected void updateButtons( Object result )
    {

    }

    @Override
    protected void clearButtons()
    {

    }
}
