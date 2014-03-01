package net.thetranquilpsychonaut.hashtagger;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by itwenty on 2/10/14.
 */
public class HashtaggerSitesAdapter extends FragmentPagerAdapter
{
    Context         ctx;
    FragmentManager fm;

    public HashtaggerSitesAdapter( FragmentManager fm, Context ctx )
    {
        super( fm );
        this.fm = fm;
        this.ctx = ctx;
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
            case 2:
                return new InstagramFragment();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return HashtaggerApp.SITES.size();
    }
}
