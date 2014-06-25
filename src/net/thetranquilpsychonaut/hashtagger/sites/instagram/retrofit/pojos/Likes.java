package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/25/14.
 */
public class Likes implements Serializable
{
    private int count;
    private List<From> data = new ArrayList<From>();

    public int getCount()
    {
        return count;
    }

    public void setCount( int count )
    {
        this.count = count;
    }

    public List<From> getData()
    {
        return data;
    }

    public void setData( List<From> data )
    {
        this.data = data;
    }

}
