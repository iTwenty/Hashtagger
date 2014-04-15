package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;


import facebook4j.Post;

/**
 * Created by itwenty on 4/15/14.
 */
public class ExpandablePost
{
    public ExpandablePost( Post post, boolean isExpanded )
    {
        this.post = post;
        this.isExpanded = isExpanded;
    }
    public Post getPost()
    {
        return post;
    }

    public boolean isExpanded()
    {
        return isExpanded;
    }

    public void setExpanded( boolean isExpanded )
    {
        this.isExpanded = isExpanded;
    }

    Post  post;
    boolean isExpanded;
}
