package net.thetranquilpsychonaut.hashtagger.events;

/**
 * Created by itwenty on 5/17/14.
 */
public class SavedHashtagDeletedEvent
{
    private String deletedHashtag;

    public SavedHashtagDeletedEvent( String deletedHashtag )
    {
        this.deletedHashtag = deletedHashtag;
    }

    public String getDeletedHashtag()
    {
        return this.deletedHashtag;
    }
}
