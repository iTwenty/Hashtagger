package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

public class TrendLocation
{
    private String    name;
    private PlaceType placeType;
    private String    url;
    private int       parentid;
    private String    country;
    private int       woeid;
    private String    countryCode;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public PlaceType getPlaceType()
    {
        return placeType;
    }

    public void setPlaceType( PlaceType placeType )
    {
        this.placeType = placeType;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public int getParentid()
    {
        return parentid;
    }

    public void setParentid( int parentid )
    {
        this.parentid = parentid;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry( String country )
    {
        this.country = country;
    }

    public int getWoeid()
    {
        return woeid;
    }

    public void setWoeid( int woeid )
    {
        this.woeid = woeid;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode( String countryCode )
    {
        this.countryCode = countryCode;
    }
}
