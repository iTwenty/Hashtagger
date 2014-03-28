package net.thetranquilpsychonaut.hashtagger.enums;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 3/28/14.
 */
public enum SearchResult
{
    SUCCESS, FAILURE;
    public static String SEARCH_RESULT_KEY = HashtaggerApp.NAMESPACE + "search_result_key";
    public static String SEARCH_RESULT_DATA = HashtaggerApp.NAMESPACE + "search_result_data";
}
