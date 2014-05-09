package net.thetranquilpsychonaut.hashtagger.widgets;

import android.content.Context;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by itwenty on 5/8/14.
 */
public class LinkClickableTextView extends TextView
{
    boolean dontConsumeNonUrlClicks = true;
    boolean linkHit;

    public LinkClickableTextView( Context context )
    {
        super( context );
    }

    public LinkClickableTextView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public LinkClickableTextView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    public boolean onTouchEvent( MotionEvent event )
    {
        linkHit = false;
        boolean res = super.onTouchEvent( event );

        if ( dontConsumeNonUrlClicks )
        {
            return linkHit;
        }
        return res;

    }

    public void setTextViewHTML( String html )
    {
        CharSequence sequence = Html.fromHtml( html );
        SpannableStringBuilder strBuilder = new SpannableStringBuilder( sequence );
        setText( strBuilder );
    }

    public static class LocalLinkMovementMethod extends LinkMovementMethod
    {
        static LocalLinkMovementMethod sInstance;


        public static LocalLinkMovementMethod getInstance()
        {
            if ( sInstance == null )
            {
                sInstance = new LocalLinkMovementMethod();
            }

            return sInstance;
        }

        @Override
        public boolean onTouchEvent( TextView widget, Spannable buffer, MotionEvent event )
        {
            int action = event.getAction();

            if ( action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN )
            {
                int x = ( int ) event.getX();
                int y = ( int ) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical( y );
                int off = layout.getOffsetForHorizontal( line, x );

                ClickableSpan[] link = buffer.getSpans( off, off, ClickableSpan.class );

                if ( link.length != 0 )
                {
                    if ( action == MotionEvent.ACTION_UP )
                    {
                        link[0].onClick( widget );
                    }
                    else if ( action == MotionEvent.ACTION_DOWN )
                    {
                        Selection.setSelection( buffer,
                                buffer.getSpanStart( link[0] ),
                                buffer.getSpanEnd( link[0] ) );
                    }

                    if ( widget instanceof LinkClickableTextView )
                    {
                        ( ( LinkClickableTextView ) widget ).linkHit = true;
                    }
                    return true;
                }
                else
                {
                    Selection.removeSelection( buffer );
                    Touch.onTouchEvent( widget, buffer, event );
                    return false;
                }
            }
            return Touch.onTouchEvent( widget, buffer, event );
        }
    }
}