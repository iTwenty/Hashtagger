package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/11/14.
 */
public class ActivityFeed implements Serializable
{
    String kind;
    String etag;
    String nextPageToken;
    String selfLink;
    String title;
    String updated;
    String id;
    List<Activity> items = new ArrayList<Activity>();

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

    public String getSelfLink()
    {
        return selfLink;
    }

    public void setSelfLink( String selfLink )
    {
        this.selfLink = selfLink;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getUpdated()
    {
        return updated;
    }

    public void setUpdated( String updated )
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

    public List<Activity> getItems()
    {
        return items;
    }

    public void setItems( List<Activity> items )
    {
        this.items = items;
    }
}
