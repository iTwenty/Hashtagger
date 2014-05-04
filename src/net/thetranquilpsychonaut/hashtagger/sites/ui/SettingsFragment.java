package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.SearchRecentSuggestions;
import net.thetranquilpsychonaut.hashtagger.HashtagSuggestionsProvider;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.SharedPreferencesHelper;

/**
 * Created by itwenty on 5/3/14.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener
{
    CheckBoxPreference cbpTwitter;
    CheckBoxPreference cbpFacebook;
    Preference         prefClearSearch;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.preferences );
        cbpTwitter = ( CheckBoxPreference ) findPreference( SharedPreferencesHelper.TWITTER_SERVICE_KEY );
        cbpFacebook = ( CheckBoxPreference ) findPreference( SharedPreferencesHelper.FACEBOOK_SERVICE_KEY );
        prefClearSearch = findPreference( SharedPreferencesHelper.CLEAR_SEARCH_KEY );
        cbpTwitter.setSummary( SharedPreferencesHelper.areTwitterDetailsPresent() ?
                               "Logged in as : " + SharedPreferencesHelper.getTwitterUserName() :
                                "Not logged in." );
        cbpFacebook.setSummary( SharedPreferencesHelper.areFacebookDetailsPresent() ?
                                "Logged in as : " + SharedPreferencesHelper.getFacebookUserName() :
                                "Not logged in." );
        prefClearSearch.setOnPreferenceClickListener( this );
    }

    @Override
    public boolean onPreferenceClick( Preference preference )
    {
        if ( preference.equals( prefClearSearch ) )
        {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                    getActivity(),
                    HashtagSuggestionsProvider.AUTHORITY,
                    HashtagSuggestionsProvider.MODE );
            suggestions.clearHistory();
        }
        return true;
    }
}
