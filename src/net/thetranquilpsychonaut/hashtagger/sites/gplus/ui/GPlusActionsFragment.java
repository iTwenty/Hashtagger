package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.PeopleFeedEvent;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.GPlus;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.ListByActivityParams;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.PeopleFeed;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Person;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesActionsFragment;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/13/14.
 */
public class GPlusActionsFragment extends SitesActionsFragment
{
    private static final String PLUSONERS_KEY = "ps";
    private static final String RESHARERS_KEY = "rs";

    private ViewPager                gPlusActionsPager;
    private GPlusActionsPagerAdapter gPlusActionsPagerAdapter;
    private Activity                 activity;

    private List<Person>   plusoners;
    private ListView       lvPlusOners;
    private TextView       lvPlusOnersEmpty;
    private PersonsAdapter plusonersAdapter;

    private List<Person>   resharers;
    private ListView       lvResharers;
    private TextView       lvResharersEmpty;
    private PersonsAdapter resharersAdapter;

    public static final GPlusActionsFragment newInstance( Activity activity )
    {
        GPlusActionsFragment f = new GPlusActionsFragment();
        Bundle b = new Bundle();
        b.putSerializable( "activity", activity );
        f.setArguments( b );
        return f;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        activity = ( Activity ) getArguments().getSerializable( "activity" );
        gPlusActionsPagerAdapter = new GPlusActionsPagerAdapter();
        plusoners = null == savedInstanceState ?
                new ArrayList<Person>() :
                ( List<Person> ) savedInstanceState.getSerializable( PLUSONERS_KEY );
        resharers = null == savedInstanceState ?
                new ArrayList<Person>() :
                ( List<Person> ) savedInstanceState.getSerializable( RESHARERS_KEY );
        plusonersAdapter = new PersonsAdapter( plusoners );
        resharersAdapter = new PersonsAdapter( resharers );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        gPlusActionsPager = ( ViewPager ) inflater.inflate( R.layout.fragment_gplus_actions, container, false );
        gPlusActionsPager.setAdapter( gPlusActionsPagerAdapter );
        return gPlusActionsPager;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState )
    {
        super.onViewCreated( view, savedInstanceState );
        if ( null == savedInstanceState )
        {
            GPlus.api().listByActivity(
                    activity.getId(),
                    ListByActivityParams.PLUSONERS,
                    null,
                    new Callback<PeopleFeed>()
                    {
                        @Override
                        public void success( PeopleFeed peopleFeed, Response response )
                        {
                            HashtaggerApp.bus.post( new PeopleFeedEvent( peopleFeed, true, ListByActivityParams.PLUSONERS ) );
                        }

                        @Override
                        public void failure( RetrofitError retrofitError )
                        {
                            HashtaggerApp.bus.post( new PeopleFeedEvent( null, false, ListByActivityParams.PLUSONERS ) );
                        }
                    } );

            GPlus.api().listByActivity(
                    activity.getId(),
                    ListByActivityParams.RESHARERS,
                    null,
                    new Callback<PeopleFeed>()
                    {
                        @Override
                        public void success( PeopleFeed peopleFeed, Response response )
                        {
                            HashtaggerApp.bus.post( new PeopleFeedEvent( peopleFeed, true, ListByActivityParams.RESHARERS ) );
                        }

                        @Override
                        public void failure( RetrofitError retrofitError )
                        {
                            HashtaggerApp.bus.post( new PeopleFeedEvent( null, false, ListByActivityParams.RESHARERS ) );
                        }
                    } );
        }
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

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putSerializable( PLUSONERS_KEY, ( java.io.Serializable ) plusoners );
        outState.putSerializable( RESHARERS_KEY, ( java.io.Serializable ) resharers );
    }

    private Object initPlusoners( ViewGroup container )
    {
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.gplus_actions_plusoners, container, false );
        lvPlusOners = ( ListView ) v.findViewById( R.id.lv_plusoners );
        lvPlusOnersEmpty = ( TextView ) v.findViewById( R.id.lv_plusoners_empty );
        lvPlusOnersEmpty.setText( "Loading" );
        lvPlusOners.setEmptyView( lvPlusOnersEmpty );
        lvPlusOners.setAdapter( plusonersAdapter );
        container.addView( v );
        return v;
    }

    private Object initResharers( ViewGroup container )
    {
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.gplus_actions_resharers, container, false );
        lvResharers = ( ListView ) v.findViewById( R.id.lv_resharers );
        lvResharersEmpty = ( TextView ) v.findViewById( R.id.lv_resharers_empty );
        lvResharersEmpty.setText( "Loading" );
        lvResharers.setEmptyView( lvResharersEmpty );
        lvResharers.setAdapter( resharersAdapter );
        container.addView( v );
        return v;
    }

    @Subscribe
    public void onActionDone( PeopleFeedEvent event )
    {
        if ( event.isSuccess() )
        {
            if ( TextUtils.equals( ListByActivityParams.PLUSONERS, event.getCollection() ) )
            {
                if ( Helper.isNullOrEmpty( event.getPeopleFeed().getItems() ) )
                {
                    lvPlusOnersEmpty.setText( "No +1'ers" );
                }
                else
                {
                    plusoners.addAll( event.getPeopleFeed().getItems() );
                    plusonersAdapter.notifyDataSetChanged();
                }
            }
            else
            {
                if ( Helper.isNullOrEmpty( event.getPeopleFeed().getItems() ) )
                {
                    lvResharersEmpty.setText( "No resharers" );
                }
                else
                {
                    resharers.addAll( event.getPeopleFeed().getItems() );
                    resharersAdapter.notifyDataSetChanged();
                }
            }
        }
        else
        {
            if ( TextUtils.equals( ListByActivityParams.RESHARERS, event.getCollection() ) )
            {
                lvPlusOnersEmpty.setText( "Failed to load +1'ers" );
            }
            else
            {
                lvResharersEmpty.setText( "Failed to load resharers" );
            }
        }
    }

    private class GPlusActionsPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public boolean isViewFromObject( View view, Object o )
        {
            return view == o;
        }

        @Override
        public CharSequence getPageTitle( int position )
        {
            switch ( position )
            {
                case 0:
                    return "+1'ers";
                case 1:
                    return "Resharers";
            }
            return null;
        }

        @Override
        public Object instantiateItem( ViewGroup container, int position )
        {
            switch ( position )
            {
                case 0:
                    return initPlusoners( container );
                case 1:
                    return initResharers( container );
            }
            return null;
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView( ( View ) object );
        }
    }

    private static class PersonsAdapter extends BaseAdapter
    {
        private List<Person> persons;

        public PersonsAdapter( List<Person> persons )
        {
            super();
            this.persons = persons;
        }

        @Override
        public int getCount()
        {
            return persons.size();
        }

        @Override
        public Object getItem( int position )
        {
            return persons.get( position );
        }

        @Override
        public long getItemId( int position )
        {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            GPlusPersonView view;
            if ( null == convertView )
            {
                view = new GPlusPersonView( parent.getContext() );
            }
            else
            {
                view = ( GPlusPersonView ) convertView;
            }
            view.update( ( Person ) getItem( position ) );
            return view;
        }
    }
}
