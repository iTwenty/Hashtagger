package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/11/14.
 */
public class SearchResult implements Serializable
{
    private List<Post> data = new ArrayList<Post>();
    private Paging paging;

    public List<Post> getData()
    {
        return data;
    }

    public void setData( List<Post> data )
    {
        this.data = data;
    }

    public Paging getPaging()
    {
        return paging;
    }

    public void setPaging( Paging paging )
    {
        this.paging = paging;
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
