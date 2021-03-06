package net.thetranquilpsychonaut.hashtagger.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

/**
 * Created by itwenty on 5/23/14.
 */
public final class DefaultPrefs
{
    public static final String ACTIVE_SITES_KEY   = "category_active_sites";
    public static final String TWITTER_SITE_KEY   = "site_twitter";
    public static final String GPLUS_SITE_KEY     = "site_gplus";
    public static final String INSTAGRAM_SITE_KEY = "site_instagram";
    public static final String FACEBOOK_SITE_KEY  = "site_facebook";
    public static final String AUTO_UPDATE_KEY    = "auto_update";
    public static final String CLEAR_SEARCH_KEY   = "clear_search";
    public static final String ABOUT_KEY          = "about";

    private static final SharedPreferences getDefaultPrefs()
    {
        return PreferenceManager.getDefaultSharedPreferences( HashtaggerApp.app );
    }

    public static boolean twitterActive      = isTwitterActive();
    public static boolean gPlusActive        = isGPlusActive();
    public static boolean instagramActive    = isInstagramActive();
    public static boolean facebookActive     = isFacebookActive();
    public static boolean activeSitesChanged = false;
    public static boolean autoUpdate         = isAutoUpdateEnabled();

    public static boolean isTwitterActive()
    {
        return getDefaultPrefs().getBoolean( TWITTER_SITE_KEY, true );
    }

    public static boolean isGPlusActive()
    {
        return getDefaultPrefs().getBoolean( GPLUS_SITE_KEY, true );
    }

    private static boolean isInstagramActive()
    {
        return getDefaultPrefs().getBoolean( INSTAGRAM_SITE_KEY, false );
    }

    public static boolean isFacebookActive()
    {
        return getDefaultPrefs().getBoolean( FACEBOOK_SITE_KEY, false );
    }

    public static boolean isAutoUpdateEnabled()
    {
        return getDefaultPrefs().getBoolean( AUTO_UPDATE_KEY, true );
    }
}
