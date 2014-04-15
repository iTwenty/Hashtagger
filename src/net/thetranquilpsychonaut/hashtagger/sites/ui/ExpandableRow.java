package net.thetranquilpsychonaut.hashtagger.sites.ui;

/**
 * Created by itwenty on 4/15/14.
 */
public interface ExpandableRow
{
    public void expandRow( Object data );

    public void showRow( Object data );

    public void collapseRow();

    public boolean isExpanded();
}
