package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.ui.FacebookFragment;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterFragment;

/**
 * Created by itwenty on 2/10/14.
 */
public class SitesAdapter extends FragmentPagerAdapter
{
    FragmentManager fm;

    public SitesAdapter( FragmentManager fm )
    {
        super( fm );
        this.fm = fm;
    }

    @Override
    public Fragment getItem( int i )
    {
        switch ( i )
        {
            case 0:
                return new TwitterFragment();
            case 1:
                return new FacebookFragment();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return HashtaggerApp.SITES.size();
    }
}
