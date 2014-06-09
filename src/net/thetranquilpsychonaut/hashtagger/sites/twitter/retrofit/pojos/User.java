package net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos;

import com.google.gson.annotations.SerializedName;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by itwenty on 6/9/14.
 */
public class User
{
    public User()
    {
        Helper.debug( "User" );
    }
    @SerializedName( "id_str" )
    private String idStr;

    private String name;

    @SerializedName( "screen_name" )
    private String screenName;

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

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString( this );
    }

    @Override
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode( this );
    }

    @Override
    public boolean equals( Object other )
    {
        return EqualsBuilder.reflectionEquals( this, other );
    }
}
