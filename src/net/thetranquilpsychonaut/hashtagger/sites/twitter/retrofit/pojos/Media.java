package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by itwenty on 6/9/14.
 */
public class Media implements Serializable
{
    @SerializedName("id_str")
    private String idStr;

    @SerializedName("media_url")
    private String mediaUrl;

    @SerializedName("media_url_https")
    private String mediaUrlHttps;

    private String url;

    @SerializedName("display_url")
    private String displayUrl;

    @SerializedName("expanded_url")
    private String expandedUrl;

    private String type;

    private Sizes sizes;

    public Sizes getSizes()
    {
        return sizes;
    }

    public void setSizes( Sizes sizes )
    {
        this.sizes = sizes;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getExpandedUrl()
    {
        return expandedUrl;
    }

    public void setExpandedUrl( String expandedUrl )
    {
        this.expandedUrl = expandedUrl;
    }

    public String getDisplayUrl()
    {
        return displayUrl;
    }

    public void setDisplayUrl( String displayUrl )
    {
        this.displayUrl = displayUrl;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getMediaUrlHttps()
    {
        return mediaUrlHttps;
    }

    public void setMediaUrlHttps( String mediaUrlHttps )
    {
        this.mediaUrlHttps = mediaUrlHttps;
    }

    public String getMediaUrl()
    {
        return mediaUrl;
    }

    public void setMediaUrl( String mediaUrl )
    {
        this.mediaUrl = mediaUrl;
    }

    public String getIdStr()
    {
        return idStr;
    }

    public void setIdStr( String idStr )
    {
        this.idStr = idStr;
    }
}
