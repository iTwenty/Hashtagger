package net.thetranquilpsychonaut.hashtagger.sites.ui;

import twitter4j.Status;

/**
 * Created by itwenty on 4/15/14.
 */
public class ExpandableStatus
{
    public ExpandableStatus( Status status, boolean isExpanded )
    {
        this.status = status;
        this.isExpanded = isExpanded;
    }
    public Status getStatus()
    {
        return status;
    }

    public boolean isExpanded()
    {
        return isExpanded;
    }

    public void setExpanded( boolean isExpanded )
    {
        this.isExpanded = isExpanded;
    }

    Status  status;
    boolean isExpanded;
}
