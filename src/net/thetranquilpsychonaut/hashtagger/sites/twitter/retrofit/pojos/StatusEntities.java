package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import java.io.Serializable;
import java.util.List;

/**
 * Created by itwenty on 6/9/14.
 */
public class StatusEntities implements Serializable
{
    private List<Media> media;

    private List<Url> urls;

    public List<Media> getMedia()
    {
        return media;
    }

    public List<Url> getUrls()
    {
        return urls;
    }
}
