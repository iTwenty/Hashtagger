package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;

public class Name implements Serializable
{

    private String familyName;
    private String givenName;

    public String getFamilyName()
    {
        return familyName;
    }

    public void setFamilyName( String familyName )
    {
        this.familyName = familyName;
    }

    public String getGivenName()
    {
        return givenName;
    }

    public void setGivenName( String givenName )
    {
        this.givenName = givenName;
    }

}
