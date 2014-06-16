package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

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
}
