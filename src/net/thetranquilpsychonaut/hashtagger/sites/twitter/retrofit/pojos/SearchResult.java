package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

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

}
