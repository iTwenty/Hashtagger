package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/11/14.
 */
public class From implements Serializable
{
    private String id;
    private String name;

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
}
