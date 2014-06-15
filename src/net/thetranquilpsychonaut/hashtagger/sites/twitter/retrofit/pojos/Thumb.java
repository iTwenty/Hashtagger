package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/9/14.
 */
public class Thumb implements Serializable
{
    private int w;

    private int h;

    private String resize;

    public int getW()
    {
        return w;
    }

    public void setW( int w )
    {
        this.w = w;
    }

    public int getH()
    {
        return h;
    }

    public void setH( int h )
    {
        this.h = h;
    }

    public String getResize()
    {
        return resize;
    }

    public void setResize( String resize )
    {
        this.resize = resize;
    }
}
