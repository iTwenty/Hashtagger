package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;

public class Thumbnail implements Serializable
{

    private String url;
    private String description;
    private Image  image;

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage( Image image )
    {
        this.image = image;
    }

}
