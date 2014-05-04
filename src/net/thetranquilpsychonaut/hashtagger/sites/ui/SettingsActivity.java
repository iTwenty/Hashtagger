package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by itwenty on 5/3/14.
 */
public class SettingsActivity extends Activity
{
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        getFragmentManager().beginTransaction().replace( android.R.id.content, new SettingsFragment() ).commit();
    }
}