package net.thetranquilpsychonaut.hashtagger.widgets;

import android.graphics.*;
import android.graphics.drawable.Drawable;

public class TextDrawable extends Drawable
{

    private final String text;
    private final Paint  paint;

    public TextDrawable( String text )
    {
        this.text = text;
        this.paint = new Paint();
        paint.setColor( Color.GRAY );
        paint.setTextSize( 16f );
        paint.setAntiAlias( true );
        paint.setFakeBoldText( true );
        paint.setShadowLayer( 6f, 0, 0, Color.BLACK );
        paint.setStyle( Paint.Style.FILL );
        paint.setTextAlign( Paint.Align.LEFT );
    }

    @Override
    public void draw( Canvas canvas )
    {
        canvas.drawText( text, 0, 0, paint );
    }

    @Override
    public void setAlpha( int alpha )
    {
        paint.setAlpha( alpha );
    }

    @Override
    public void setColorFilter( ColorFilter cf )
    {
        paint.setColorFilter( cf );
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSLUCENT;
    }
}
