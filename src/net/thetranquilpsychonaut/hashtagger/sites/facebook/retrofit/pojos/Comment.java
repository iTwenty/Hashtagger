package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by itwenty on 6/11/14.
 */
public class Comment implements Serializable
{
    private String  id;
    private From    from;
    private String  message;
    @SerializedName( "can_remove" )
    private boolean canRemove;
    @SerializedName( "created_time" )
    private Date    createdTime;
    @SerializedName( "like_count" )
    private int     likeCount;
    @SerializedName( "user_likes" )
    private boolean userLikes;

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

    public boolean isCanRemove()
    {
        return canRemove;
    }

    public void setCanRemove( boolean canRemove )
    {
        this.canRemove = canRemove;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime( Date createdTime )
    {
        this.createdTime = createdTime;
    }

    public int getLikeCount()
    {
        return likeCount;
    }

    public void setLikeCount( int likeCount )
    {
        this.likeCount = likeCount;
    }

    public boolean isUserLikes()
    {
        return userLikes;
    }

    public void setUserLikes( boolean userLikes )
    {
        this.userLikes = userLikes;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString( this );
    }

    @Override
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode( this );
    }

    @Override
    public boolean equals( Object other )
    {
        return EqualsBuilder.reflectionEquals( this, other );
    }
}
