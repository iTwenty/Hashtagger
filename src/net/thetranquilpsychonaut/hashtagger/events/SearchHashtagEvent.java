package net.thetranquilpsychonaut.hashtagger.events;

/**
 * Created by itwenty on 6/6/14.
 */
public class SearchHashtagEvent
{
    String hashtag;

    public SearchHashtagEvent( String hashtag )
    {
        this.hashtag = hashtag;
    }

    public String getHashtag()
    {
        return hashtag;
    }
}
