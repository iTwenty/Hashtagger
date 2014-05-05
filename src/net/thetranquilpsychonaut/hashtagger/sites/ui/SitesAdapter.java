package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.support.v4.app.FragmentManager;
import net.thetranquilpsychonaut.hashtagger.cwacpager.ArrayPagerAdapter;
import net.thetranquilpsychonaut.hashtagger.cwacpager.PageDescriptor;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.ui.FacebookFragment;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.ui.GPlusFragment;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterFragment;

import java.util.ArrayList;

/**
 * Created by itwenty on 2/10/14.
 */
public class SitesAdapter extends ArrayPagerAdapter<SitesFragment>
{
    public SitesAdapter( FragmentManager fm, ArrayList<PageDescriptor> descriptors )
    {
        super( fm, descriptors );
    }

    @Override
    protected SitesFragment createFragment( PageDescriptor pageDescriptor )
    {
        if ( pageDescriptor.equals( TwitterFragment.descriptor ) )
        {
            return new TwitterFragment();
        }
        if ( pageDescriptor.equals( FacebookFragment.descriptor ) )
        {
            return new FacebookFragment();
        }
        if ( pageDescriptor.equals( GPlusFragment.descriptor ) )
        {
            return new GPlusFragment();
        }
        return null;
    }
}
