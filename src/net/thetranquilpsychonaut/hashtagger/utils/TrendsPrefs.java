package net.thetranquilpsychonaut.hashtagger.utils;

import android.content.Context;
import android.content.SharedPreferences;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterTrendsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by itwenty on 6/4/14.
 */
public class TrendsPrefs
{
    public static final String TRENDS_PREFS                   = "trends_prefs";
    public static final String LOCAL_TRENDS_KEY               = "local_trends";
    public static final String LOCAL_TRENDS_LAST_UPDATED_KEY  = "local_trends_last_updated";
    public static final String GLOBAL_TRENDS_KEY              = "global_trends";
    public static final String GLOBAL_TRENDS_LAST_UPDATED_KEY = "global_trends_last_updated";
    public static final String TRENDS_CHOICE                  = "trends_choice";

    private static SharedPreferences getTrendsPrefs()
    {
        return HashtaggerApp.app.getSharedPreferences( TRENDS_PREFS, Context.MODE_PRIVATE );
    }

    // Local Trends

    public static long getLocalTrendsLastUpdated()
    {
        return getTrendsPrefs().getLong( LOCAL_TRENDS_LAST_UPDATED_KEY, -1L );
    }

    public static List<String> getLocalTrends()
    {
        return new ArrayList<String>( getTrendsPrefs().getStringSet( LOCAL_TRENDS_KEY, Collections.<String>emptySet() ) );
    }

    public static void setLocalTrends( List<String> localTrends )
    {
        getTrendsPrefs().edit()
                .putStringSet( LOCAL_TRENDS_KEY, new HashSet<String>( localTrends ) )
                .putLong( LOCAL_TRENDS_LAST_UPDATED_KEY, System.currentTimeMillis() )
                .commit();
    }

    // Global trends

    public static long getGlobalTrendsLastUpdated()
    {
        return getTrendsPrefs().getLong( GLOBAL_TRENDS_LAST_UPDATED_KEY, -1 );
    }

    public static List<String> getGlobalTrends()
    {
        return new ArrayList<String>( getTrendsPrefs().getStringSet( GLOBAL_TRENDS_KEY, Collections.<String>emptySet() ) );
    }

    public static void setGlobalTrends( List<String> globalTrends )
    {
        getTrendsPrefs().edit()
                .putStringSet( GLOBAL_TRENDS_KEY, new HashSet<String>( globalTrends ) )
                .putLong( GLOBAL_TRENDS_LAST_UPDATED_KEY, System.currentTimeMillis() )
                .commit();
    }

    // Trends choice

    public static int getTrendsChoice()
    {
        return getTrendsPrefs().getInt( TRENDS_CHOICE, TwitterTrendsService.LOCAL );
    }

    public static void setTrendsChoice( int choice )
    {
        getTrendsPrefs().edit()
                .putInt( TRENDS_CHOICE, choice )
                .commit();
    }
}
