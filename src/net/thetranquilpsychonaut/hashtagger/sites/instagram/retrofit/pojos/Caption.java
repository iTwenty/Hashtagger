package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/24/14.
 */
public class Caption implements Serializable
{
    private String text;

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }
}
