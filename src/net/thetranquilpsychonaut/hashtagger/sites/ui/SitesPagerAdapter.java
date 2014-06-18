package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.support.v4.app.FragmentManager;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.cwacpager.ArrayPagerAdapter;
import net.thetranquilpsychonaut.hashtagger.cwacpager.PageDescriptor;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.ui.FacebookFragment;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.ui.GPlusFragment;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.ui.TwitterFragment;
import net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator.IconPagerAdapter;

import java.util.ArrayList;

/**
 * Created by itwenty on 2/10/14.
 */
public class SitesPagerAdapter extends ArrayPagerAdapter<SitesFragment> implements IconPagerAdapter
{
    public SitesPagerAdapter( FragmentManager fm, ArrayList<PageDescriptor> descriptors )
    {
        super( fm, descriptors );
    }

    @Override
    protected SitesFragment createFragment( PageDescriptor pageDescriptor )
    {
        if ( pageDescriptor.equals( TwitterFragment.DESCRIPTOR ) )
        {
            return new TwitterFragment();
        }
        if ( pageDescriptor.equals( GPlusFragment.DESCRIPTOR ) )
        {
            return new GPlusFragment();
        }
        if ( pageDescriptor.equals( FacebookFragment.DESCRIPTOR ) )
        {
            return new FacebookFragment();
        }
        return null;
    }

    @Override
    public int getIconResId( int position )
    {
        PageDescriptor descriptor = getEntries().get( position ).getDescriptor();
        if ( descriptor.equals( TwitterFragment.DESCRIPTOR ) )
        {
            return R.drawable.twitter_logo;
        }
        if ( descriptor.equals( GPlusFragment.DESCRIPTOR ) )
        {
            return R.drawable.gplus_logo;
        }
        if ( descriptor.equals( FacebookFragment.DESCRIPTOR ) )
        {
            return R.drawable.facebook_logo;
        }
        return -1;
    }

    @Override
    public int getSelectedColor( int position )
    {
        PageDescriptor descriptor = getEntries().get( position ).getDescriptor();
        if ( descriptor.equals( TwitterFragment.DESCRIPTOR ) )
        {
            return R.color.twitter_blue;
        }
        if ( descriptor.equals( GPlusFragment.DESCRIPTOR ) )
        {
            return R.color.gplus_red;
        }
        if ( descriptor.equals( FacebookFragment.DESCRIPTOR ) )
        {
            return R.color.facebook_blue;
        }
        return -1;
    }
}
