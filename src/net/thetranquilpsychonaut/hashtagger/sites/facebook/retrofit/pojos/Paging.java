package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by itwenty on 6/11/14.
 */
public class Paging implements Serializable
{
    private String previous;
    private String next;

    public String getPrevious()
    {
        return previous;
    }

    public void setPrevious( String previous )
    {
        this.previous = previous;
    }

    public String getNext()
    {
        return next;
    }

    public void setNext( String next )
    {
        this.next = next;
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
