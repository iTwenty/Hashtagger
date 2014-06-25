package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by itwenty on 6/25/14.
 */
public class Comment implements Serializable
{
    @SerializedName("created_time")
    private long   createdTime;
    private String text;
    private From   from;
    private String id;

    public long getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime( long createdTime )
    {
        this.createdTime = createdTime;
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public From getFrom()
    {
        return from;
    }

    public void setFrom( From from )
    {
        this.from = from;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }
}
