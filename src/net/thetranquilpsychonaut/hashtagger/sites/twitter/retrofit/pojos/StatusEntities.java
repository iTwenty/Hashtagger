package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by itwenty on 6/9/14.
 */
public class StatusEntities
{
    public StatusEntities()
    {
        Helper.debug( "StatusEntities" );
    }
    private List<Media> media;

    private List<Url> urls;

    public List<Media> getMedia()
    {
        return media;
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
