package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by itwenty on 6/25/14.
 */
public class Pagination implements Serializable
{
    @SerializedName("next_max_tag_id")
    private String nextMaxTagId;
    @SerializedName("next_max_id")
    private String nextMaxId;
    @SerializedName("next_min_id")
    private String nextMinId;
    @SerializedName("min_tag_id")
    private String minTagId;
    @SerializedName("next_url")
    private String nextUrl;

    public String getNextMaxTagId()
    {
        return nextMaxTagId;
    }

    public void setNextMaxTagId( String nextMaxTagId )
    {
        this.nextMaxTagId = nextMaxTagId;
    }

    public String getNextMaxId()
    {
        return nextMaxId;
    }

    public void setNextMaxId( String nextMaxId )
    {
        this.nextMaxId = nextMaxId;
    }

    public String getNextMinId()
    {
        return nextMinId;
    }

    public void setNextMinId( String nextMinId )
    {
        this.nextMinId = nextMinId;
    }

    public String getMinTagId()
    {
        return minTagId;
    }

    public void setMinTagId( String minTagId )
    {
        this.minTagId = minTagId;
    }

    public String getNextUrl()
    {
        return nextUrl;
    }

    public void setNextUrl( String nextUrl )
    {
        this.nextUrl = nextUrl;
    }
}
