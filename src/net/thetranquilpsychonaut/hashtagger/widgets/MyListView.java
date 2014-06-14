package net.thetranquilpsychonaut.hashtagger.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by itwenty on 6/13/14.
 */
public class MyListView extends ListView
{
    private boolean scrollable = true;

    public MyListView( Context context )
    {
        super( context );
    }

    public MyListView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public MyListView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    public void setScrollable( boolean scrollable )
    {
        this.scrollable = scrollable;
    }

    @Override
    public boolean dispatchTouchEvent( MotionEvent ev )
    {
        if ( ev.getAction() == MotionEvent.ACTION_MOVE && !scrollable )
        {
            return false;
        }
        return super.dispatchTouchEvent( ev );
    }

    @Override
    public boolean dispatchGenericMotionEvent( MotionEvent ev )
    {
        if ( ev.getAction() == MotionEvent.ACTION_SCROLL && !scrollable )
        {
            return false;
        }
        return super.dispatchGenericMotionEvent( ev );
    }
}
