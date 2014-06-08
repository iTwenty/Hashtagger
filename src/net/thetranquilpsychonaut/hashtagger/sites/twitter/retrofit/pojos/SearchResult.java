package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by itwenty on 6/8/14.
 */
public class SearchResult implements Serializable
{
    @SerializedName( "statuses" )
    private List<Tweet> statuses;

    @SerializedName( "search_metadata" )
    private SearchMetadata searchMetadata;

    public List<Tweet> getStatuses()
    {
        return statuses;
    }

    public void setStatuses( List<Tweet> statuses )
    {
        this.statuses = statuses;
    }

    @Override
    public String toString()
    {
        return (( Object ) this ).getClass().getName() + " [ " +
                "statuses = " + statuses + " , " +
                "searchMetadata = " + searchMetadata + " ] ";
    }
}
