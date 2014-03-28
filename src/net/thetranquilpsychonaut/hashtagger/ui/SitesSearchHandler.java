package net.thetranquilpsychonaut.hashtagger.ui;

import net.thetranquilpsychonaut.hashtagger.enums.SearchType;

/**
 * Created by itwenty on 3/14/14.
 */
public interface SitesSearchHandler
{
    public void beginSearch( SearchType searchType );
    public void destroySearch();
}
