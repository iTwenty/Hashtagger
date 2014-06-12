package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import android.text.Spannable;
import net.thetranquilpsychonaut.hashtagger.utils.Linkifier;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPlusObject implements Serializable
{

    private String      objectType;
    private String      id;
    private ObjectActor actor;
    private String      content;
    private String      originalContent;
    private String      url;
    private Replies     replies;
    private Plusoners   plusoners;
    private Resharers   resharers;

    private transient Spannable linkedText;
    private List<Attachment> attachments = new ArrayList<Attachment>();

    public String getObjectType()
    {
        return objectType;
    }

    public void setObjectType( String objectType )
    {
        this.objectType = objectType;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public ObjectActor getActor()
    {
        return actor;
    }

    public void setActor( ObjectActor actor )
    {
        this.actor = actor;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }

    public String getOriginalContent()
    {
        return originalContent;
    }

    public void setOriginalContent( String originalContent )
    {
        this.originalContent = originalContent;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public Replies getReplies()
    {
        return replies;
    }

    public void setReplies( Replies replies )
    {
        this.replies = replies;
    }

    public Plusoners getPlusoners()
    {
        return plusoners;
    }

    public void setPlusoners( Plusoners plusoners )
    {
        this.plusoners = plusoners;
    }

    public Resharers getResharers()
    {
        return resharers;
    }

    public void setResharers( Resharers resharers )
    {
        this.resharers = resharers;
    }

    public List<Attachment> getAttachments()
    {
        return attachments;
    }

    public void setAttachments( List<Attachment> attachments )
    {
        this.attachments = attachments;
    }

    public Spannable getLinkedText()
    {
        return linkedText;
    }

    public void setLinkedText( Spannable linkedText )
    {
        this.linkedText = linkedText;
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

    private void readObject( ObjectInputStream inputStream ) throws IOException, ClassNotFoundException
    {
        inputStream.defaultReadObject();
        linkedText = Linkifier.getLinkedGPlusText( content );
    }
}
