package net.thetranquilpsychonaut.hashtagger;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity implements ActionBar.TabListener
{
    /**
     * Called when the activity is first created.
     */
    ActionBar actionBar;
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        actionBar = getActionBar( );
        actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );

        for( int i = 0; i < HashtaggerApp.SITES.size( ); ++i )
        {
            actionBar.addTab( actionBar.newTab( ).setText( HashtaggerApp.SITES.get( i ) ).setTabListener( this ) );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater( ).inflate( R.menu.options_menu, menu );
        return true;
    }

    @Override
    public void onTabSelected( ActionBar.Tab tab, FragmentTransaction ft )
    {

    }

    @Override
    public void onTabUnselected( ActionBar.Tab tab, FragmentTransaction ft )
    {

    }

    @Override
    public void onTabReselected( ActionBar.Tab tab, FragmentTransaction ft )
    {

    }
}
