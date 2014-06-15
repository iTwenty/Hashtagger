package net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/11/14.
 */
public class Paging implements Serializable
{
    private String previous;
    private String next;

    public String getPrevious()
    {
        return previous;
    }

    public void setPrevious( String previous )
    {
        this.previous = previous;
    }

    public String getNext()
    {
        return next;
    }

    public void setNext( String next )
    {
        this.next = next;
    }
}
