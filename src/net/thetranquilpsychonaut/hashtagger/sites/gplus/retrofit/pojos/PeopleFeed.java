package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/14/14.
 */
public class PeopleFeed implements Serializable
{
    private String kind;
    private String etag;
    private String selfLink;
    private String title;
    private String nextPageToken;
    private int    totalItems;
    private List<Person> items = new ArrayList<Person>();

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

    public String getNextPageToken()
    {
        return nextPageToken;
    }

    public void setNextPageToken( String nextPageToken )
    {
        this.nextPageToken = nextPageToken;
    }

    public int getTotalItems()
    {
        return totalItems;
    }

    public void setTotalItems( int totalItems )
    {
        this.totalItems = totalItems;
    }

    public List<Person> getItems()
    {
        return items;
    }

    public void setItems( List<Person> items )
    {
        this.items = items;
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
    public boolean equals( java.lang.Object other )
    {
        return EqualsBuilder.reflectionEquals( this, other );
    }
}
