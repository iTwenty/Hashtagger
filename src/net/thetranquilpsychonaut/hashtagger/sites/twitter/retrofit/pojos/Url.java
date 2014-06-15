package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by itwenty on 6/9/14.
 */
public class Url implements Serializable
{
    private String url;

    @SerializedName("expanded_url")
    private String expandedUrl;

    @SerializedName("display_url")
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
}
