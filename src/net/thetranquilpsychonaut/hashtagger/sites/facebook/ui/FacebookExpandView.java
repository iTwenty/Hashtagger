package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.ViewAnimator;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/18/14.
 */
public class FacebookExpandView extends RelativeLayout
{
    private ViewAnimator       vaFacebookExpandView;
    private FacebookObjectView facebookObjectView;
    private FacebookDetailView facebookDetailView;
    private FacebookButtons    facebookButtons;

    public FacebookExpandView( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookExpandView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookExpandView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        LayoutInflater.from( context ).inflate( R.layout.facebook_expand_view, this, true );
        vaFacebookExpandView = ( ViewAnimator ) findViewById( R.id.va_facebook_expand_view );
        facebookObjectView = ( FacebookObjectView ) findViewById( R.id.facebook_object_view );
        facebookDetailView = ( FacebookDetailView ) findViewById( R.id.facebook_detail_view );
        facebookButtons = ( FacebookButtons ) findViewById( R.id.facebook_buttons );
        vaFacebookExpandView.setVisibility( GONE );
    }

    public void showPost( Post post )
    {
        boolean hasObject = null != post.getObjectId();
        boolean hasDetails = !( "status".equals( post.getType() ) ) && null == post.getObjectId();
        if ( hasObject )
        {
            vaFacebookExpandView.setVisibility( VISIBLE );
            vaFacebookExpandView.setDisplayedChild( 0 );
            facebookObjectView.showObjectFromPost( post );
        }
        else if(  hasDetails )
        {
            vaFacebookExpandView.setVisibility( VISIBLE );
            vaFacebookExpandView.setDisplayedChild( 1 );
            facebookDetailView.showDetailsFromPost( post );
        }
        else
        {
            vaFacebookExpandView.setVisibility( GONE );
        }
        facebookButtons.setVisibility( VISIBLE );
    }
}
