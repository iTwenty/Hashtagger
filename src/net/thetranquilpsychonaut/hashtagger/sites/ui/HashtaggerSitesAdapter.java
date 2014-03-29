package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.ui.FacebookFragment;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.ui.InstagramFragment;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterFragment;

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
