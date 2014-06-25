package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by itwenty on 6/25/14.
 */
public class Videos implements Serializable
{
    @SerializedName("low_bandwidth")
    private Video lowBandwidth;
    @SerializedName("low_resolution")
    private Video lowResolution;
    @SerializedName("standard_resolution")
    private Video standardResolution;

    public Video getLowBandwidth()
    {
        return lowBandwidth;
    }

    public void setLowBandwidth( Video lowBandwidth )
    {
        this.lowBandwidth = lowBandwidth;
    }

    public Video getLowResolution()
    {
        return lowResolution;
    }

    public void setLowResolution( Video lowResolution )
    {
        this.lowResolution = lowResolution;
    }

    public Video getStandardResolution()
    {
        return standardResolution;
    }

    public void setStandardResolution( Video standardResolution )
    {
        this.standardResolution = standardResolution;
    }
}
