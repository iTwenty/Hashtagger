package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/11/14.
 */
public class Likes implements Serializable
{
    private List<From> data = new ArrayList<From>();

    public List<From> getData()
    {
        return data;
    }

    public void setData( List<From> data )
    {
        this.data = data;
    }
}
