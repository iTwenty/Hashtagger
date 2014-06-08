package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by itwenty on 6/7/14.
 */
public class Tweet implements Serializable
{
    @SerializedName( "text" )
    private String text;

    public void setText( String text )
    {
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }

    @Override
    public String toString()
    {
        return ( ( Object ) this ).getClass().getName() + " [ " +
                "text = " + text + " , " + " ] ";
    }
}
