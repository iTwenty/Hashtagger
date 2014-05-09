package net.thetranquilpsychonaut.hashtagger.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.Button;
import net.thetranquilpsychonaut.hashtagger.R;

public class CenterContentButton extends Button
{
    private ImageSpan centerSpan;

    public CenterContentButton( Context context )
    {
        this( context, null, 0 );
    }

    public CenterContentButton( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public CenterContentButton( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        TypedArray ta = context.obtainStyledAttributes( attrs, R.styleable.CenterContentButton );
        centerSpan = new ImageSpan( ta.getDrawable( R.styleable.CenterContentButton_drawableCenter ) );
        ta.recycle();
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        super.onDraw( canvas );
        SpannableStringBuilder ssb = new SpannableStringBuilder( getText() );
        ssb.insert( 0, " " );
        ssb.setSpan( centerSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        setText( ssb );
    }
}