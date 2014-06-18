package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import android.text.Spannable;
import com.google.gson.annotations.SerializedName;
import net.thetranquilpsychonaut.hashtagger.utils.Linkifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by itwenty on 6/9/14.
 */
public class Status implements Serializable
{
    @SerializedName( "created_at" )
    private Date createdAt;

    @SerializedName("id_str")
    private String idStr;

    private String text;

    private transient Spannable linkedText;

    @SerializedName("in_reply_to_status_id_str")
    private String inReplyToStatusIdStr;

    @SerializedName("in_reply_to_user_id_str")
    private String inReplyToUserIdStr;

    @SerializedName("in_reply_to_screen_name")
    private String inReplyToScreenName;

    private User user;

    @SerializedName("retweet_count")
    private int retweetCount;

    @SerializedName("favorite_count")
    private int favoriteCount;

    private StatusEntities entities;

    private boolean favorited;

    private boolean retweeted;

    @SerializedName("retweeted_status")
    private Status retweetedStatus;

    public Status getRetweetedStatus()
    {
        return retweetedStatus;
    }

    public void setRetweetedStatus( Status retweetedStatus )
    {
        this.retweetedStatus = retweetedStatus;
    }

    public boolean isRetweeted()
    {
        return retweeted;
    }

    public void setRetweeted( boolean retweeted )
    {
        this.retweeted = retweeted;
    }

    public boolean isFavorited()
    {
        return favorited;
    }

    public void setFavorited( boolean favorited )
    {
        this.favorited = favorited;
    }

    public StatusEntities getEntities()
    {
        return entities;
    }

    public void setEntities( StatusEntities entities )
    {
        this.entities = entities;
    }

    public int getFavoriteCount()
    {
        return favoriteCount;
    }

    public void setFavoriteCount( int favoriteCount )
    {
        this.favoriteCount = favoriteCount;
    }

    public int getRetweetCount()
    {
        return retweetCount;
    }

    public void setRetweetCount( int retweetCount )
    {
        this.retweetCount = retweetCount;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public String getInReplyToScreenName()
    {
        return inReplyToScreenName;
    }

    public void setInReplyToScreenName( String inReplyToScreenName )
    {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public String getInReplyToUserIdStr()
    {
        return inReplyToUserIdStr;
    }

    public void setInReplyToUserIdStr( String inReplyToUserIdStr )
    {
        this.inReplyToUserIdStr = inReplyToUserIdStr;
    }

    public String getInReplyToStatusIdStr()
    {
        return inReplyToStatusIdStr;
    }

    public void setInReplyToStatusIdStr( String inReplyToStatusIdStr )
    {
        this.inReplyToStatusIdStr = inReplyToStatusIdStr;
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public String getIdStr()
    {
        return idStr;
    }

    public void setIdStr( String idStr )
    {
        this.idStr = idStr;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt( Date createdAt )
    {
        this.createdAt = createdAt;
    }

    public boolean isRetweet()
    {
        if ( null == retweetedStatus )
        {
            return false;
        }
        return true;
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
        linkedText = Linkifier.getLinkedTwitterText( text );
    }
}
