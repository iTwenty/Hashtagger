package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Access implements Serializable
{

    private String kind;
    private String description;
    private List<Item> items = new ArrayList<Item>();

    public String getKind()
    {
        return kind;
    }

    public void setKind( String kind )
    {
        this.kind = kind;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public List<Item> getItems()
    {
        return items;
    }

    public void setItems( List<Item> items )
    {
        this.items = items;
    }
}
