package net.thetranquilpsychonaut.hashtagger;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by itwenty on 4/18/14.
 */
public class HashtagSuggestionsProvider extends SearchRecentSuggestionsProvider
{
    public static final String AUTHORITY = HashtaggerApp.NAMESPACE + "HashtagSuggestionsProvider";
    public static final int    MODE      = DATABASE_MODE_QUERIES;

    public HashtagSuggestionsProvider()
    {
        setupSuggestions( AUTHORITY, MODE );
    }
}
