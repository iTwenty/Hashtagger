package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/24/14.
 */
public class Media implements Serializable
{
    private String  type;
    private Caption caption;

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public Caption getCaption()
    {
        return caption;
    }

    public void setCaption( Caption caption )
    {
        this.caption = caption;
    }
}
