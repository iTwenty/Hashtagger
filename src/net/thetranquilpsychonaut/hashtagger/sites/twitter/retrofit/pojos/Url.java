package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by itwenty on 6/9/14.
 */
public class Url
{
    public Url()
    {
        Helper.debug( "Url called" );
    }

    private String  url;

    @SerializedName( "expanded_url" )
    private String expandedUrl;

    @SerializedName( "display_url" )
    private String displayUrl;

    public String getDisplayUrl()
    {
        return displayUrl;
    }

    public void setDisplayUrl( String displayUrl )
    {
        this.displayUrl = displayUrl;
    }

    public String getExpandedUrl()
    {
        return expandedUrl;
    }

    public void setExpandedUrl( String expandedUrl )
    {
        this.expandedUrl = expandedUrl;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
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
