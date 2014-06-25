package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/25/14.
 */
public class Video implements Serializable
{
    private String url;
    private int    width;
    private int    height;

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth( int width )
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight( int height )
    {
        this.height = height;
    }
}
