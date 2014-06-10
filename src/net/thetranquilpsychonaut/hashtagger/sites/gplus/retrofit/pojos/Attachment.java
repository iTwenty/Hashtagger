package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Attachment
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

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString( this );
    }

    @Override
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode( this );
    }

    @Override
    public boolean equals( java.lang.Object other )
    {
        return EqualsBuilder.reflectionEquals( this, other );
    }

}
