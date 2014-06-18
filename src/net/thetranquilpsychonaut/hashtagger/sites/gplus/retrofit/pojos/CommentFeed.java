package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by itwenty on 6/15/14.
 */
public class CommentFeed implements Serializable
{
    private String kind;
    private String etag;
    private String nextPageToken;
    private String nextLink;
    private String title;
    private Date   updated;
    private String id;
    private List<Comment> items = new ArrayList<Comment>();

    public String getKind()
    {
        return kind;
    }

    public void setKind( String kind )
    {
        this.kind = kind;
    }

    public String getEtag()
    {
        return etag;
    }

    public void setEtag( String etag )
    {
        this.etag = etag;
    }

    public String getNextPageToken()
    {
        return nextPageToken;
    }

    public void setNextPageToken( String nextPageToken )
    {
        this.nextPageToken = nextPageToken;
    }

    public String getNextLink()
    {
        return nextLink;
    }

    public void setNextLink( String nextLink )
    {
        this.nextLink = nextLink;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated( Date updated )
    {
        this.updated = updated;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public List<Comment> getItems()
    {
        return items;
    }

    public void setItems( List<Comment> items )
    {
        this.items = items;
    }
}
