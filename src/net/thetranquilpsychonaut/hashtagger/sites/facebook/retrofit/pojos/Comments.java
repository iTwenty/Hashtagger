package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/11/14.
 */
public class Comments implements Serializable
{
    private List<Comment> data = new ArrayList<Comment>();

    public List<Comment> getData()
    {
        return data;
    }

    public void setData( List<Comment> data )
    {
        this.data = data;
    }
}
