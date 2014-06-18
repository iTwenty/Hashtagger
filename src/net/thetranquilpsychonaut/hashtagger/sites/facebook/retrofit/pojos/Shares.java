package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/11/14.
 */
public class Shares implements Serializable
{
    private int count;

    public int getCount()
    {
        return count;
    }

    public void setCount( int count )
    {
        this.count = count;
    }
}
