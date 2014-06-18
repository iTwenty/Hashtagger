package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import android.text.Spannable;
import com.google.gson.annotations.SerializedName;
import net.thetranquilpsychonaut.hashtagger.utils.Linkifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by itwenty on 6/11/14.
 */
public class Post implements Serializable
{
    private String id;
    private From   from;
    private String message;
    private String picture;
    private String link;
    private String name;
    private String description;
    private String caption;
    private String source;
    private String type;
    @SerializedName("object_id")
    private
    String objectId;
    @SerializedName("created_time")
    private
    Date   createdTime;
    @SerializedName("updated_time")
    private
    Date   updatedTime;
    private Likes    likes;
    private Shares   shares;
    private Comments comments;

    private transient Spannable linkedText;

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public From getFrom()
    {
        return from;
    }

    public void setFrom( From from )
    {
        this.from = from;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture( String picture )
    {
        this.picture = picture;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink( String link )
    {
        this.link = link;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getCaption()
    {
        return caption;
    }

    public void setCaption( String caption )
    {
        this.caption = caption;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource( String source )
    {
        this.source = source;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId( String objectId )
    {
        this.objectId = objectId;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime( Date createdTime )
    {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime()
    {
        return updatedTime;
    }

    public void setUpdatedTime( Date updatedTime )
    {
        this.updatedTime = updatedTime;
    }

    public Likes getLikes()
    {
        return likes;
    }

    public void setLikes( Likes likes )
    {
        this.likes = likes;
    }

    public Shares getShares()
    {
        return shares;
    }

    public void setShares( Shares shares )
    {
        this.shares = shares;
    }

    public Comments getComments()
    {
        return comments;
    }

    public void setComments( Comments comments )
    {
        this.comments = comments;
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
        linkedText = Linkifier.getLinkedFacebookText( message );
    }
}
