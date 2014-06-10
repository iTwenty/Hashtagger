package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by itwenty on 6/8/14.
 */
public class SearchMetadata implements Serializable
{
    @SerializedName("max_id_str")
    private String maxIdStr;

    @SerializedName("query")
    private String query;

    @SerializedName("count")
    private int count;

    @SerializedName("since_id_str")
    private String sinceIdStr;

    public String getSinceIdStr()
    {
        return sinceIdStr;
    }

    public int getCount()
    {
        return count;
    }

    public String getQuery()
    {
        return query;
    }

    public String getMaxIdStr()
    {
        return maxIdStr;
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
