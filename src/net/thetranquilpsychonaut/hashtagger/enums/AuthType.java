package net.thetranquilpsychonaut.hashtagger.enums;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 4/2/14.
 */
public enum AuthType
{
    REQUEST, ACCESS;
    public static String AUTH_TYPE_KEY = HashtaggerApp.NAMESPACE + "auth_type_key";
}
