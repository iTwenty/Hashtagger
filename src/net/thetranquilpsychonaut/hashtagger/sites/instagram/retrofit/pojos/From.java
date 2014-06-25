package net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by itwenty on 6/25/14.
 */
public class From implements Serializable
{
    @SerializedName("username")
    private String userName;
    @SerializedName("profile_picture")
    private String profilePicture;
    private String id;
    @SerializedName("full_name")
    private String fullName;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public String getProfilePicture()
    {
        return profilePicture;
    }

    public void setProfilePicture( String profilePicture )
    {
        this.profilePicture = profilePicture;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName( String fullName )
    {
        this.fullName = fullName;
    }
}
