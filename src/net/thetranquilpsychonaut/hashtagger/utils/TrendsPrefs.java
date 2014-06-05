package net.thetranquilpsychonaut.hashtagger.utils;

import android.content.Context;
import android.content.SharedPreferences;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by itwenty on 6/4/14.
 */
public class TrendsPrefs
{
    public static final String TRENDS_PREFS            = "trends_prefs";
    public static final String PLACE_KEY               = "place";
    public static final String TRENDS_KEY              = "trends";
    public static final String TRENDS_LAST_UPDATED_KEY = "trends_last_updated";

    private static SharedPreferences getTrendsPrefs()
    {
        return HashtaggerApp.app.getSharedPreferences( TRENDS_PREFS, Context.MODE_PRIVATE );
    }

    public static long getTrendsLastUpdated()
    {
        return getTrendsPrefs().getLong( TRENDS_LAST_UPDATED_KEY, -1L );
    }

    public static String getPlace()
    {
        return getTrendsPrefs().getString( PLACE_KEY, "" );
    }

    public static List<String> getTrends()
    {
        return new ArrayList<String>( getTrendsPrefs().getStringSet( TRENDS_KEY, Collections.<String>emptySet() ) );
    }

    public static void addTrends( List<String> trends, String place )
    {
        getTrendsPrefs().edit()
                .putStringSet( TRENDS_KEY, new HashSet<String>( trends ) )
                .putString( PLACE_KEY, place )
                .putLong( TRENDS_LAST_UPDATED_KEY, System.currentTimeMillis() )
                .commit();
    }
}
