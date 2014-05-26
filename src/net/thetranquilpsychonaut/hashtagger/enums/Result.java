package net.thetranquilpsychonaut.hashtagger.enums;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 3/28/14.
 */
public enum Result
{
    SUCCESS, FAILURE;
    public static final String RESULT_KEY    = HashtaggerApp.NAMESPACE + "result_key";
    public static final String RESULT_DATA   = HashtaggerApp.NAMESPACE + "result_data";
    public static final String RESULT_EXTRAS = HashtaggerApp.NAMESPACE + "result_extras";
}
