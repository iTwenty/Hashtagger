package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by itwenty on 6/15/14.
 */
public class Comment implements Serializable
{
    private String        kind;
    private String        etag;
    private String        verb;
    private String        id;
    private Date          published;
    private Date          updated;
    private ObjectActor   actor;
    private CommentObject object;
    private String        selfLink;
    private Plusoners     plusoners;
    private List<InReplyTo> inReplyTo = new ArrayList<InReplyTo>();

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

    public String getVerb()
    {
        return verb;
    }

    public void setVerb( String verb )
    {
        this.verb = verb;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public Date getPublished()
    {
        return published;
    }

    public void setPublished( Date published )
    {
        this.published = published;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated( Date updated )
    {
        this.updated = updated;
    }

    public ObjectActor getActor()
    {
        return actor;
    }

    public void setActor( ObjectActor actor )
    {
        this.actor = actor;
    }

    public CommentObject getObject()
    {
        return object;
    }

    public void setObject( CommentObject object )
    {
        this.object = object;
    }

    public String getSelfLink()
    {
        return selfLink;
    }

    public void setSelfLink( String selfLink )
    {
        this.selfLink = selfLink;
    }

    public Plusoners getPlusoners()
    {
        return plusoners;
    }

    public void setPlusoners( Plusoners plusoners )
    {
        this.plusoners = plusoners;
    }

    public List<InReplyTo> getInReplyTo()
    {
        return inReplyTo;
    }

    public void setInReplyTo( List<InReplyTo> inReplyTo )
    {
        this.inReplyTo = inReplyTo;
    }
}
