package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/24/14.
 */
public class Media implements Serializable
{
    private List<String> tags = new ArrayList<String>();
    private Videos   videos;
    private String   type;
    private Comments comments;
    private String   filter;
    @SerializedName( "created_time" )
    private long     createdTime;
    private String   link;
    private Likes    likes;
    private Images   images;
    private Caption  caption;
    @SerializedName( "user_has_liked" )
    private boolean  userHasLiked;
    private String   id;
    private From     user;

    public List<String> getTags()
    {
        return tags;
    }

    public void setTags( List<String> tags )
    {
        this.tags = tags;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public Comments getComments()
    {
        return comments;
    }

    public void setComments( Comments comments )
    {
        this.comments = comments;
    }

    public String getFilter()
    {
        return filter;
    }

    public void setFilter( String filter )
    {
        this.filter = filter;
    }

    public long getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime( long createdTime )
    {
        this.createdTime = createdTime;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink( String link )
    {
        this.link = link;
    }

    public Likes getLikes()
    {
        return likes;
    }

    public void setLikes( Likes likes )
    {
        this.likes = likes;
    }

    public Images getImages()
    {
        return images;
    }

    public void setImages( Images images )
    {
        this.images = images;
    }

    public Caption getCaption()
    {
        return caption;
    }

    public void setCaption( Caption caption )
    {
        this.caption = caption;
    }

    public boolean isUserHasLiked()
    {
        return userHasLiked;
    }

    public void setUserHasLiked( boolean userHasLiked )
    {
        this.userHasLiked = userHasLiked;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public From getUser()
    {
        return user;
    }

    public void setUser( From user )
    {
        this.user = user;
    }
}
