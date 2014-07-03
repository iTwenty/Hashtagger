package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.provider.SearchRecentSuggestions;
import net.thetranquilpsychonaut.hashtagger.HashtagSuggestionsProvider;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.DefaultPrefs;

/**
 * Created by itwenty on 5/3/14.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener
{
    // Max sites that can be active simultaneously.
    private static final int MAX_ACTIVE_SITES = 4;

    PreferenceCategory activeSites;
    CheckBoxPreference cbpTwitter;
    CheckBoxPreference cbpGPlus;
    CheckBoxPreference cbpInstagram;
    CheckBoxPreference cbpFacebook;
    CheckBoxPreference cbpAutoUpdate;
    Preference         prefClearSearch;
    Preference         prefAbout;

    private int activeSitesCount;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.preferences );

        activeSites = ( PreferenceCategory ) findPreference( DefaultPrefs.ACTIVE_SITES_KEY );
        cbpTwitter = ( CheckBoxPreference ) findPreference( DefaultPrefs.TWITTER_SITE_KEY );
        cbpGPlus = ( CheckBoxPreference ) findPreference( DefaultPrefs.GPLUS_SITE_KEY );
        cbpInstagram = ( CheckBoxPreference ) findPreference( DefaultPrefs.INSTAGRAM_SITE_KEY );
        cbpFacebook = ( CheckBoxPreference ) findPreference( DefaultPrefs.FACEBOOK_SITE_KEY );
        cbpAutoUpdate = ( CheckBoxPreference ) findPreference( DefaultPrefs.AUTO_UPDATE_KEY );
        prefClearSearch = findPreference( DefaultPrefs.CLEAR_SEARCH_KEY );
        prefAbout = findPreference( DefaultPrefs.ABOUT_KEY );

        disableUncheckedSitesIfMaxActiveSitesReached();

        cbpTwitter.setSummary( AccountPrefs.areTwitterDetailsPresent() ?
                "Logged in as : " + AccountPrefs.getTwitterUserName() :
                "Not logged in." );
        cbpGPlus.setSummary( AccountPrefs.areGPlusDetailsPresent() ?
                "Logged in as : " + AccountPrefs.getGPlusUserName() :
                "Not logged in." );
        cbpInstagram.setSummary( AccountPrefs.areInstagramDetailsPresent() ?
                "Logged in as : " + AccountPrefs.getInstagramUserName() :
                "Not logged in." );
        cbpFacebook.setSummary( AccountPrefs.areFacebookDetailsPresent() ?
                "Logged in as : " + AccountPrefs.getFacebookUserName() :
                "Not logged in." );

        setAutoUpdateSummary( DefaultPrefs.isAutoUpdateEnabled() );

        cbpTwitter.setOnPreferenceChangeListener( this );
        cbpGPlus.setOnPreferenceChangeListener( this );
        cbpInstagram.setOnPreferenceChangeListener( this );
        cbpFacebook.setOnPreferenceChangeListener( this );
        cbpAutoUpdate.setOnPreferenceChangeListener( this );
        prefClearSearch.setOnPreferenceClickListener( this );
        prefAbout.setOnPreferenceClickListener( this );
    }

    private void disableUncheckedSitesIfMaxActiveSitesReached()
    {
        updateActiveSitesCount();
        activeSites.setTitle( String.format( getString( R.string.actives_sites_title ), activeSitesCount, MAX_ACTIVE_SITES ) );
        if ( activeSitesCount >= MAX_ACTIVE_SITES )
        {
            if ( !DefaultPrefs.twitterActive )
            {
                cbpTwitter.setEnabled( false );
            }
            if ( !DefaultPrefs.gPlusActive )
            {
                cbpGPlus.setEnabled( false );
            }
            if ( !DefaultPrefs.instagramActive )
            {
                cbpInstagram.setEnabled( false );
            }
            if ( !DefaultPrefs.facebookActive )
            {
                cbpFacebook.setEnabled( false );
            }
        }
        else
        {
            cbpTwitter.setEnabled( true );
            cbpGPlus.setEnabled( true );
            cbpInstagram.setEnabled( true );
            cbpFacebook.setEnabled( true );
        }
    }

    private void updateActiveSitesCount()
    {
        // Reset the count before updating
        activeSitesCount = 0;

        if ( DefaultPrefs.twitterActive )
        {
            activeSitesCount++;
        }
        if ( DefaultPrefs.gPlusActive )
        {
            activeSitesCount++;
        }
        if ( DefaultPrefs.instagramActive )
        {
            activeSitesCount++;
        }
        if ( DefaultPrefs.facebookActive )
        {
            activeSitesCount++;
        }
    }

    @Override
    public boolean onPreferenceClick( Preference preference )
    {
        if ( preference.equals( prefClearSearch ) )
        {
            new AlertDialog.Builder( getActivity() ).setMessage( "Clear search history?" )
                    .setPositiveButton( "Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick( DialogInterface dialog, int which )
                        {
                            doClearRecentSearch();
                        }
                    } )
                    .setNegativeButton( "No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick( DialogInterface dialog, int which )
                        {
                            dialog.dismiss();
                        }
                    } )
                    .show();
        }
        else if ( preference.equals( prefAbout ) )
        {
            getActivity().getFragmentManager()
                    .beginTransaction()
                    .add( AboutDialog.newInstance(), AboutDialog.TAG )
                    .commit();
        }
        return true;
    }

    private void doClearRecentSearch()
    {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                getActivity(),
                HashtagSuggestionsProvider.AUTHORITY,
                HashtagSuggestionsProvider.MODE );
        suggestions.clearHistory();
    }

    @Override
    public boolean onPreferenceChange( Preference preference, Object newValue )
    {
        if ( preference.equals( cbpTwitter ) )
        {
            DefaultPrefs.twitterActive = ( Boolean ) newValue;
            DefaultPrefs.activeSitesChanged = true;
            disableUncheckedSitesIfMaxActiveSitesReached();
        }
        if ( preference.equals( cbpGPlus ) )
        {
            DefaultPrefs.gPlusActive = ( Boolean ) newValue;
            DefaultPrefs.activeSitesChanged = true;
            disableUncheckedSitesIfMaxActiveSitesReached();
        }
        if ( preference.equals( cbpInstagram ) )
        {
            DefaultPrefs.instagramActive = ( Boolean ) newValue;
            DefaultPrefs.activeSitesChanged = true;
            disableUncheckedSitesIfMaxActiveSitesReached();
        }
        if ( preference.equals( cbpFacebook ) )
        {
            DefaultPrefs.facebookActive = ( Boolean ) newValue;
            DefaultPrefs.activeSitesChanged = true;
            disableUncheckedSitesIfMaxActiveSitesReached();
        }
        if ( preference.equals( cbpAutoUpdate ) )
        {
            setAutoUpdateSummary( ( Boolean ) newValue );
            DefaultPrefs.autoUpdate = ( Boolean ) newValue;
        }
        return true;
    }

    private void setAutoUpdateSummary( boolean autoUpdate )
    {
        cbpAutoUpdate.setSummary( autoUpdate ? "Auto update is on" : "Auto update is off" );
    }
}
