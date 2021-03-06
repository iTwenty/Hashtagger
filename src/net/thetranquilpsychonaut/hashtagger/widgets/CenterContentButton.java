package net.thetranquilpsychonaut.hashtagger.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;

public class CenterContentButton extends Button
{
    private int leftPadding = -1;

    public CenterContentButton( Context context )
    {
        super( context );
    }

    public CenterContentButton( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public CenterContentButton( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void onLayout( boolean changed, int left, int top, int right, int bottom )
    {
        super.onLayout( changed, left, top, right, bottom );
        setPadding( calculateLeftPadding(), getPaddingTop(), getPaddingRight(), getPaddingBottom() );

    }

    private int calculateLeftPadding()
    {
        if ( leftPadding == -1 )
        {
            int textWidth = 0;
            if ( !TextUtils.isEmpty( getText() ) )
            {
                textWidth = ( int ) getPaint().measureText( getText().toString() );
            }
            int drawableWidth = 0;
            if ( null != getCompoundDrawables()[0] )
            {
                drawableWidth = getCompoundDrawables()[0].getIntrinsicWidth();
            }
            leftPadding = ( getWidth() - ( drawableWidth + textWidth ) ) / 2 - getPaddingLeft();
        }
        return leftPadding;
    }
}