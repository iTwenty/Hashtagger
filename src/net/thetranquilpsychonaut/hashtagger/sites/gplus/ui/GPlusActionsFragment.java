package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesActionsFragment;

/**
 * Created by itwenty on 6/13/14.
 */
public class GPlusActionsFragment extends SitesActionsFragment
{
    private ViewPager                gPlusActionsPager;
    private GPlusActionsPagerAdapter gPlusActionsPagerAdapter;
    private Activity                 activity;

    public static final GPlusActionsFragment newInstance( Activity activity )
    {
        GPlusActionsFragment f = new GPlusActionsFragment();
        Bundle b = new Bundle();
        b.putSerializable( "activity", activity );
        f.setArguments( b );
        return f;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        gPlusActionsPager = ( ViewPager ) inflater.inflate( R.layout.fragment_gplus_actions, container, false );
        gPlusActionsPagerAdapter = new GPlusActionsPagerAdapter();
        gPlusActionsPager.setAdapter( gPlusActionsPagerAdapter );
        activity = ( Activity ) getArguments().getSerializable( "activity" );
        return gPlusActionsPager;
    }

    private class GPlusActionsPagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return 1;
        }

        @Override
        public boolean isViewFromObject( View view, Object o )
        {
            return view == o;
        }

        @Override
        public Object instantiateItem( ViewGroup container, int position )
        {
            switch ( position )
            {
                case 0:
                    TextView lvGPlusOnesEmpty = new TextView( container.getContext() );
                    lvGPlusOnesEmpty.setText( "Empty" );
                    container.addView( lvGPlusOnesEmpty, 0 );
                    return container;
            }
            return null;
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView( ( View ) object );
        }
    }
}
