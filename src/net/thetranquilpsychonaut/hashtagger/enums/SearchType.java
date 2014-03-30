package net.thetranquilpsychonaut.hashtagger.enums;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 3/28/14.
 */
public enum SearchType
{
    INITIAL, OLDER, NEWER;
    public static String SEARCH_TYPE_KEY = HashtaggerApp.NAMESPACE + "search_type_key";
}
