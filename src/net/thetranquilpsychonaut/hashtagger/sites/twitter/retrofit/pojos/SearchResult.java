package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by itwenty on 6/8/14.
 */
public class SearchResult implements Serializable
{
    private List<Status> statuses;

    @SerializedName("search_metadata")
    private SearchMetadata searchMetadata;

    public List<Status> getStatuses()
    {
        return statuses;
    }

    public void setStatuses( List<Status> statuses )
    {
        this.statuses = statuses;
    }

    public SearchMetadata getSearchMetadata()
    {
        return searchMetadata;
    }

    public void setSearchMetadata( SearchMetadata searchMetadata )
    {
        this.searchMetadata = searchMetadata;
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
