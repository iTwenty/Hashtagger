package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import android.text.Spannable;
import com.google.gson.annotations.SerializedName;
import net.thetranquilpsychonaut.hashtagger.utils.Linkifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by itwenty on 6/24/14.
 */
public class Caption implements Serializable
{
    @SerializedName("created_time")
    private           long      createdTime;
    private           String    text;
    private transient Spannable linkedText;
    private           From      from;
    private           String    id;

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

    public Spannable getLinkedText()
    {
        return linkedText;
    }

    public void setLinkedText( Spannable linkedText )
    {
        this.linkedText = linkedText;
    }

    private void readObject( ObjectInputStream inputStream ) throws IOException, ClassNotFoundException
    {
        inputStream.defaultReadObject();
        linkedText = Linkifier.getLinkedInstagramText( text );
    }
}
