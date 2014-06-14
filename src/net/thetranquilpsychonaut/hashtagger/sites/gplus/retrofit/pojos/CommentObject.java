package net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos;

import java.io.Serializable;

/**
 * Created by itwenty on 6/15/14.
 */
public class CommentObject implements Serializable
{
    private String objectType;
    private String content;
    private String originalContent;

    public String getObjectType()
    {
        return objectType;
    }

    public void setObjectType( String objectType )
    {
        this.objectType = objectType;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }

    public String getOriginalContent()
    {
        return originalContent;
    }

    public void setOriginalContent( String originalContent )
    {
        this.originalContent = originalContent;
    }
}
