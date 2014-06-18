package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;

public class ObjectActor implements Serializable
{

    private String     id;
    private String     displayName;
    private String     url;
    private ActorImage image;

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
