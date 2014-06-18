package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/11/14.
 */
public class SearchResult implements Serializable
{
    private List<Post> data = new ArrayList<Post>();
    private Paging paging;

    public List<Post> getData()
    {
        return data;
    }

    public void setData( List<Post> data )
    {
        this.data = data;
    }

    public Paging getPaging()
    {
        return paging;
    }

    public void setPaging( Paging paging )
    {
        this.paging = paging;
    }
}
