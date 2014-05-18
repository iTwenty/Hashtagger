package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

/**
 * Created by itwenty on 5/18/14.
 */
public abstract class BaseActivity extends FragmentActivity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        getActionBar().setDisplayHomeAsUpEnabled( true );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch ( item.getItemId() )
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }
}
