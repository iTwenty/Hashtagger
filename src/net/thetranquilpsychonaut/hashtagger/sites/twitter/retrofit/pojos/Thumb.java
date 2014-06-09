package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by itwenty on 6/9/14.
 */
public class Thumb
{
    private int w;

    private int h;

    private String resize;

    public int getW()
    {
        return w;
    }

    public void setW( int w )
    {
        this.w = w;
    }

    public int getH()
    {
        return h;
    }

    public void setH( int h )
    {
        this.h = h;
    }

    public String getResize()
    {
        return resize;
    }

    public void setResize( String resize )
    {
        this.resize = resize;
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
