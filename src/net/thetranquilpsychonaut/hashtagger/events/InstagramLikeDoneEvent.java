package net.thetranquilpsychonaut.hashtagger.events;

import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;

/**
 * Created by itwenty on 7/2/14.
 */
public class InstagramLikeDoneEvent extends ResultEvent
{
    private Media media;

    public InstagramLikeDoneEvent( boolean success, Media media )
    {
        super( success );
        this.media = media;
    }

    public Media getMedia()
    {
        return this.media;
    }
}
