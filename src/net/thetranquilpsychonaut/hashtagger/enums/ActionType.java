package net.thetranquilpsychonaut.hashtagger.enums;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 4/2/14.
 */
public enum ActionType
{
    SEARCH, AUTH;
    public static final String ACTION_TYPE_KEY = HashtaggerApp.NAMESPACE + "action_type_key";
}
