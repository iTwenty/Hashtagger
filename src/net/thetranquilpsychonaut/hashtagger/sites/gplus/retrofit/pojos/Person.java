package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/14/14.
 */
public class Person implements Serializable
{
    private String     kind;
    private String     etag;
    private String     id;
    private String     displayName;
    private String     url;
    private ActorImage image;

    public String getKind()
    {
        return kind;
    }

    public void setKind( String kind )
    {
        this.kind = kind;
    }

    public String getEtag()
    {
        return etag;
    }

    public void setEtag( String etag )
    {
        this.etag = etag;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public ActorImage getImage()
    {
        return image;
    }

    public void setImage( ActorImage image )
    {
        this.image = image;
    }
}
