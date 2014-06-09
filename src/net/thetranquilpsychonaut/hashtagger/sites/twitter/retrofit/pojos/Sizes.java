package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by itwenty on 6/9/14.
 */
public class Sizes implements Serializable
{
    public Sizes()
    {
        Helper.debug( "Sizes" );
    }
    private Small   small;

    private Large   large;

    private Thumb   thumb;

    private Medium medium;

    public Small getSmall()
    {
        return small;
    }

    public void setSmall( Small small )
    {
        this.small = small;
    }

    public Large getLarge()
    {
        return large;
    }

    public void setLarge( Large large )
    {
        this.large = large;
    }

    public Thumb getThumb()
    {
        return thumb;
    }

    public void setThumb( Thumb thumb )
    {
        this.thumb = thumb;
    }

    public Medium getMedium()
    {
        return medium;
    }

    public void setMedium( Medium medium )
    {
        this.medium = medium;
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
