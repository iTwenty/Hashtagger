package net.thetranquilpsychonaut.hashtagger.enums;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 4/2/14.
 */
public final class AuthType
{
    public static final int    REQUEST       = 0;
    public static final int    ACCESS        = 1;
    public static final String AUTH_TYPE_KEY = HashtaggerApp.NAMESPACE + "auth_type_key";
}
