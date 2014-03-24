package net.thetranquilpsychonaut.hashtagger.otto;

/**
 * Created by itwenty on 3/18/14.
 */
public class HashtagEvent
{
    private final String hashtag;

    public HashtagEvent( String hashtag )
    {
        this.hashtag = hashtag;
    }

    public String getHashtag()
    {
        return this.hashtag;
    }

}
