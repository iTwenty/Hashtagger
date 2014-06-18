package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by itwenty on 6/9/14.
 */
public class User implements Serializable
{
    @SerializedName("id_str")
    private String idStr;

    private String name;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("profile_image_url")
    private String profileImageUrl;

    public String getIdStr()
    {
        return idStr;
    }

    public void setIdStr( String idStr )
    {
        this.idStr = idStr;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getScreenName()
    {
        return screenName;
    }

    public void setScreenName( String screenName )
    {
        this.screenName = screenName;
    }

    public String getProfileImageUrl()
    {
        return profileImageUrl;
    }

    public void setProfileImageUrl( String profileImageUrl )
    {
        this.profileImageUrl = profileImageUrl;
    }
}
