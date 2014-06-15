package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/9/14.
 */
public class Sizes implements Serializable
{
    private Small small;

    private Large large;

    private Thumb thumb;

    private Medium medium;

    public Small getSmall()
    {
        return small;
    }

    public void setSmall( Small small )
    {
        this.small = small;
    }

    public Large getLarge()
    {
        return large;
    }

    public void setLarge( Large large )
    {
        this.large = large;
    }

    public Thumb getThumb()
    {
        return thumb;
    }

    public void setThumb( Thumb thumb )
    {
        this.thumb = thumb;
    }

    public Medium getMedium()
    {
        return medium;
    }

    public void setMedium( Medium medium )
    {
        this.medium = medium;
    }
}
