package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trends
{
    private List<Trend> trends = new ArrayList<Trend>();
    @SerializedName("as_of")
    private Date asOf;
    @SerializedName("created_at")
    private Date createdAt;
    private List<TrendLocationShort> locations = new ArrayList<TrendLocationShort>();

    public List<Trend> getTrends()
    {
        return trends;
    }

    public void setTrends( List<Trend> trends )
    {
        this.trends = trends;
    }

    public Date getAsOf()
    {
        return asOf;
    }

    public void setAsOf( Date asOf )
    {
        this.asOf = asOf;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt( Date createdAt )
    {
        this.createdAt = createdAt;
    }

    public List<TrendLocationShort> getLocations()
    {
        return locations;
    }

    public void setLocations( List<TrendLocationShort> locations )
    {
        this.locations = locations;
    }

}
