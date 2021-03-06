package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Attachment implements Serializable
{

    private String objectType;
    private String displayName;
    private String id;
    private String content;
    private String url;
    private Image  image;
    private Image  fullImage;
    private Embed  embed;
    private List<Thumbnail> thumbnails = new ArrayList<Thumbnail>();

    public String getObjectType()
    {
        return objectType;
    }

    public void setObjectType( String objectType )
    {
        this.objectType = objectType;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage( Image image )
    {
        this.image = image;
    }

    public Image getFullImage()
    {
        return fullImage;
    }

    public void setFullImage( Image fullImage )
    {
        this.fullImage = fullImage;
    }

    public Embed getEmbed()
    {
        return embed;
    }

    public void setEmbed( Embed embed )
    {
        this.embed = embed;
    }

    public List<Thumbnail> getThumbnails()
    {
        return thumbnails;
    }

    public void setThumbnails( List<Thumbnail> thumbnails )
    {
        this.thumbnails = thumbnails;
    }

}
