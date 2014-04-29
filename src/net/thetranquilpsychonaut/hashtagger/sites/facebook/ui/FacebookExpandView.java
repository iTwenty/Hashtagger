package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.ViewAnimator;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.components.ViewExpander;

/**
 * Created by itwenty on 4/18/14.
 */
public class FacebookExpandView extends RelativeLayout
{
    private ViewAnimator       vaFacebookExpandView;
    private FacebookObjectView facebookObjectView;
    private FacebookDetailView facebookDetailView;
    private FacebookButtons    facebookButtons;
    private ViewExpander       expander;

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
        inflate( context, R.layout.facebook_expand_view, this );
        vaFacebookExpandView = ( ViewAnimator ) findViewById( R.id.va_facebook_expand_view );
        facebookObjectView = ( FacebookObjectView ) findViewById( R.id.facebook_object_view );
        facebookDetailView = ( FacebookDetailView ) findViewById( R.id.facebook_detail_view );
        facebookButtons = ( FacebookButtons ) findViewById( R.id.facebook_buttons );
        vaFacebookExpandView.setVisibility( GONE );
        expander = new ViewExpander( this );
    }

    public void expandPost( Post post, int postType, boolean animate )
    {
        switch ( postType )
        {
            case FacebookListAdapter.POST_TYPE_OBJECT:
                vaFacebookExpandView.setVisibility( VISIBLE );
                vaFacebookExpandView.setDisplayedChild( 0 );
                facebookObjectView.showObjectFromPost( post );
                break;
            case FacebookListAdapter.POST_TYPE_DETAILS:
                vaFacebookExpandView.setVisibility( VISIBLE );
                vaFacebookExpandView.setDisplayedChild( 1 );
                facebookDetailView.showDetailsFromPost( post );
                break;
            case FacebookListAdapter.POST_TYPE_NORMAL:
                vaFacebookExpandView.setVisibility( GONE );
                break;
        }
        facebookButtons.setVisibility( VISIBLE );
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec( LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY );
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec( LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY );
        measure( widthMeasureSpec, heightMeasureSpec );
        expander.expandView( getMeasuredHeight(), animate );
    }

    public void collapsePost( boolean animate )
    {
        expander.collapseView( animate );
    }
}
