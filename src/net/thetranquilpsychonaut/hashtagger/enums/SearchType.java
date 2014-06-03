package net.thetranquilpsychonaut.hashtagger.enums;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 3/28/14.
 */
public class SearchType
{
    public static final int    INITIAL         = 0;
    public static final int    OLDER           = 1;
    public static final int    NEWER           = 2;
    public static final int    TIMED           = 3;
    public static final String SEARCH_TYPE_KEY = HashtaggerApp.NAMESPACE + "search_type_key";
}
