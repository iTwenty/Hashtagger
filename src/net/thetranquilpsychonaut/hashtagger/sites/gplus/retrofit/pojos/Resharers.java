package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;

public class Resharers implements Serializable
{

    private int    totalItems;
    private String selfLink;

    public int getTotalItems()
    {
        return totalItems;
    }

    public void setTotalItems( int totalItems )
    {
        this.totalItems = totalItems;
    }

    public String getSelfLink()
    {
        return selfLink;
    }

    public void setSelfLink( String selfLink )
    {
        this.selfLink = selfLink;
    }

}
