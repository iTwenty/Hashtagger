package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;
import java.util.Date;

public class Activity implements Serializable
{

    private String      kind;
    private String      etag;
    private String      title;
    private Date        published;
    private Date        updated;
    private String      id;
    private String      url;
    private Actor       actor;
    private String      verb;
    private GPlusObject object;
    private String      annotation;
    private String      crosspostSource;
    private Provider    provider;
    private Access      access;
    private String      geocode;
    private String      address;
    private String      radius;
    private String      placeId;

    private String placeName;

    private Location location;

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

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
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

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public Actor getActor()
    {
        return actor;
    }

    public void setActor( Actor actor )
    {
        this.actor = actor;
    }

    public String getVerb()
    {
        return verb;
    }

    public void setVerb( String verb )
    {
        this.verb = verb;
    }

    public GPlusObject getObject()
    {
        return object;
    }

    public void setObject( GPlusObject object )
    {
        this.object = object;
    }

    public String getAnnotation()
    {
        return annotation;
    }

    public void setAnnotation( String annotation )
    {
        this.annotation = annotation;
    }

    public String getCrosspostSource()
    {
        return crosspostSource;
    }

    public void setCrosspostSource( String crosspostSource )
    {
        this.crosspostSource = crosspostSource;
    }

    public Provider getProvider()
    {
        return provider;
    }

    public void setProvider( Provider provider )
    {
        this.provider = provider;
    }

    public Access getAccess()
    {
        return access;
    }

    public void setAccess( Access access )
    {
        this.access = access;
    }

    public String getGeocode()
    {
        return geocode;
    }

    public void setGeocode( String geocode )
    {
        this.geocode = geocode;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress( String address )
    {
        this.address = address;
    }

    public String getRadius()
    {
        return radius;
    }

    public void setRadius( String radius )
    {
        this.radius = radius;
    }

    public String getPlaceId()
    {
        return placeId;
    }

    public void setPlaceId( String placeId )
    {
        this.placeId = placeId;
    }

    public String getPlaceName()
    {
        return placeName;
    }

    public void setPlaceName( String placeName )
    {
        this.placeName = placeName;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation( Location location )
    {
        this.location = location;
    }

}
