package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/25/14.
 */
public class SearchResult implements Serializable
{
    private Pagination pagination;
    private Meta       meta;
    private List<Media> data = new ArrayList<Media>();

    public Pagination getPagination()
    {
        return pagination;
    }

    public void setPagination( Pagination pagination )
    {
        this.pagination = pagination;
    }

    public Meta getMeta()
    {
        return meta;
    }

    public void setMeta( Meta meta )
    {
        this.meta = meta;
    }

    public List<Media> getData()
    {
        return data;
    }

    public void setData( List<Media> data )
    {
        this.data = data;
    }
}
