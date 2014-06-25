package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import net.thetranquilpsychonaut.hashtagger.sites.retrofit.MapParams;

/**
 * Created by itwenty on 6/25/14.
 */
public class SearchParams extends MapParams
{
    private static final String COUNT_KEY      = "count";
    private static final String MIN_TAG_ID_KEY = "min_tag_id";
    private static final String MAX_TAG_ID_KEY = "max_tag_id";

    public SearchParams()
    {
        super();
    }

    public SearchParams setCount( int count )
    {
        getParams().put( COUNT_KEY, Integer.toString( count ) );
        return this;
    }

    public SearchParams setMinTagId( String minTagId )
    {
        getParams().put( MIN_TAG_ID_KEY, minTagId );
        return this;
    }

    public SearchParams setMaxTagId( String maxTagId )
    {
        getParams().put( MAX_TAG_ID_KEY, maxTagId );
        return this;
    }
}
