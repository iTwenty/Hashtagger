package net.thetranquilpsychonaut.hashtagger.widgets;

import android.graphics.*;
import android.graphics.drawable.Drawable;

public class TextDrawable extends Drawable
{

    private final String text;
    private final Paint  paint;
    private       int    textWidth;
    private       int    textHeight;

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
        Rect rect = new Rect();
        paint.getTextBounds( text, 0, text.length(), rect );
        textWidth = rect.width();
        textHeight = rect.height();
    }

    @Override
    public void draw( Canvas canvas )
    {
        canvas.drawText( text, ( canvas.getWidth() - textWidth ) / 2, ( canvas.getHeight() - textHeight ) / 2, paint );
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
