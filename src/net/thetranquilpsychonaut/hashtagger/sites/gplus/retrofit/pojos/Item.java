package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;

public class Item implements Serializable
{

    private String type;
    private String id;
    private String displayName;

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

}
