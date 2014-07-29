package net.thetranquilpsychonaut.hashtagger.sites.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator.IconPagerAdapter;
import net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator.IconPagerIndicator;

/**
 * Created by itwenty on 6/16/14.
 */
public abstract class SitesActionsFragment extends DialogFragment
{
    protected ViewPager                sitesActionsPager;
    protected SitesActionsPagerAdapter sitesActionsPagerAdapter;
    protected IconPagerIndicator       sitesActionsPagerIndicator;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        sitesActionsPagerAdapter = initSitesActionsPagerAdapter();
    }

    protected abstract SitesActionsPagerAdapter initSitesActionsPagerAdapter();

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        Dialog d = super.onCreateDialog( savedInstanceState );
        d.requestWindowFeature( Window.FEATURE_NO_TITLE );
        return d;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_sites_actions, container, false );
        sitesActionsPager = ( ViewPager ) v.findViewById( R.id.sites_actions_pager );
        sitesActionsPagerIndicator = ( IconPagerIndicator ) v.findViewById( R.id.sites_actions_pager_indicator );
        sitesActionsPager.setAdapter( sitesActionsPagerAdapter );
        sitesActionsPagerIndicator.setViewPager( sitesActionsPager );
        sitesActionsPager.setOffscreenPageLimit( Integer.MAX_VALUE );
        sitesActionsPager.setCurrentItem( getSelectedAction() );
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        HashtaggerApp.bus.register( this );
    }

    @Override
    public void onStop()
    {
        super.onStop();
        HashtaggerApp.bus.unregister( this );
    }

    public IconPagerIndicator getSitesActionsPagerIndicator()
    {
        return this.sitesActionsPagerIndicator;
    }

    protected abstract int getSelectedAction();

    protected abstract class SitesActionsPagerAdapter extends PagerAdapter implements IconPagerAdapter
    {
        @Override
        public int getSelectedColor( int position )
        {
            return R.color.orange;
        }

        @Override
        public boolean isViewFromObject( View view, Object o )
        {
            return view == o;
        }

        @Override
        public abstract Object instantiateItem( ViewGroup container, int position );

        @Override
        public abstract void destroyItem( ViewGroup container, int position, Object object );
    }
}
