package net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator;

/**
 * Created by itwenty on 6/16/14.
 */
public interface IconPagerAdapter
{
    public int getIconResId( int position );

    public int getSelectedColor( int position );

    public int getCount();
}
