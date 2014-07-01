package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/25/14.
 */
public class Comments implements Serializable
{
    private int count;
    private List<Comment> data = new ArrayList<Comment>();

    public int getCount()
    {
        return count;
    }

    public void setCount( int count )
    {
        this.count = count;
    }

    public List<Comment> getData()
    {
        return data;
    }

    public void setData( List<Comment> data )
    {
        this.data = data;
    }
}
