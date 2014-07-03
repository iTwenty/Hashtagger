package net.thetranquilpsychonaut.hashtagger.sites.twitter.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetListEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.Twitter;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesActionsFragment;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator.IconPagerAdapter;
import net.thetranquilpsychonaut.hashtagger.widgets.iconpagerindicator.IconPagerIndicator;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 7/3/14.
 */
public class TwitterActionsFragment extends SitesActionsFragment implements AdapterView.OnItemClickListener
{
    private static final String RETWEETS_KEY = "rt";
    public static final  String TAG          = TwitterActionsFragment.class.getSimpleName();

    private ViewPager                  twitterActionsPager;
    private IconPagerIndicator         twitterActionsPagerIndicator;
    private TwitterActionsPagerAdapter twitterActionsPagerAdapter;

    private List<Status>    retweets;
    private ListView        lvRetweets;
    private TextView        lvRetweetsEmpty;
    private RetweetsAdapter retweetsAdapter;

    private Status status;
    private int    actionType;

    public static TwitterActionsFragment newInstance( Status status, int actionType )
    {
        TwitterActionsFragment fragment = new TwitterActionsFragment();
        Bundle b = new Bundle();
        b.putSerializable( "status", status );
        b.putInt( "actionType", actionType );
        fragment.setArguments( b );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        status = ( Status ) getArguments().getSerializable( "status" );
        actionType = getArguments().getInt( "actionType" );
        retweets = null == savedInstanceState ?
                new ArrayList<Status>() :
                ( List<Status> ) savedInstanceState.getSerializable( RETWEETS_KEY );
        retweetsAdapter = new RetweetsAdapter( retweets );
        twitterActionsPagerAdapter = new TwitterActionsPagerAdapter();
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
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View v = inflater.inflate( R.layout.fragment_twitter_actions, container, false );
        twitterActionsPager = ( ViewPager ) v.findViewById( R.id.twitter_actions_pager );
        twitterActionsPagerIndicator = ( IconPagerIndicator ) v.findViewById( R.id.twitter_actions_pager_indicator );
        twitterActionsPager.setAdapter( twitterActionsPagerAdapter );
        twitterActionsPagerIndicator.setViewPager( twitterActionsPager );
        return v;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState )
    {
        super.onViewCreated( view, savedInstanceState );
        if ( null == savedInstanceState )
        {
            twitterActionsPager.post( new Runnable()
            {
                @Override
                public void run()
                {
                    if ( status.getRetweetCount() != 0 )
                    {
                        lvRetweetsEmpty.setText( getResources().getString( R.string.str_loading ) );
                        Twitter.api().getRetweets( status.getIdStr(), new TwitterRetweetCallback() );
                    }
                }
            } );
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putSerializable( RETWEETS_KEY, ( java.io.Serializable ) retweets );
    }

    private Object initRetweets( ViewGroup container )
    {
        View v = LayoutInflater.from( container.getContext() ).inflate( R.layout.twitter_actions_retweet, container, false );
        lvRetweets = ( ListView ) v.findViewById( R.id.lv_retweets );
        lvRetweetsEmpty = ( TextView ) v.findViewById( R.id.lv_retweets_empty );
        lvRetweetsEmpty.setText( "No retweets" );
        lvRetweets.setEmptyView( lvRetweetsEmpty );
        lvRetweets.setAdapter( retweetsAdapter );
        lvRetweets.setOnItemClickListener( this );
        container.addView( v );
        return v;
    }

    @Subscribe
    public void onTwitterRetweetListDone( TwitterRetweetListEvent event )
    {
        if ( event.isSuccess() )
        {
            if ( Helper.isNullOrEmpty( event.getRetweets() ) )
            {
                lvRetweetsEmpty.setText( "No retweets" );
            }
            else
            {
                retweets.clear();
                retweets.addAll( event.getRetweets() );
                retweetsAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            lvRetweetsEmpty.setText( "Failed to load retweets" );
        }
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {

    }

    private class TwitterActionsPagerAdapter extends PagerAdapter implements IconPagerAdapter
    {
        @Override
        public int getIconResId( int position )
        {
            return R.drawable.retweet;
        }

        @Override
        public int getSelectedColor( int position )
        {
            return R.color.orange;
        }

        @Override
        public int getCount()
        {
            return 1;
        }

        @Override
        public Object instantiateItem( ViewGroup container, int position )
        {
            return initRetweets( container );
        }

        @Override
        public void destroyItem( ViewGroup container, int position, Object object )
        {
            container.removeView( ( View ) object );
        }

        @Override
        public boolean isViewFromObject( View view, Object o )
        {
            return view == o;
        }
    }

    private static class RetweetsAdapter extends BaseAdapter
    {
        private List<Status> retweets;

        public RetweetsAdapter( List<Status> retweets )
        {
            this.retweets = retweets;
        }

        @Override
        public int getCount()
        {
            return retweets.size();
        }

        @Override
        public Object getItem( int position )
        {
            return retweets.get( position );
        }

        @Override
        public long getItemId( int position )
        {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            TwitterRetweetView view;
            if ( null == convertView )
            {
                view = new TwitterRetweetView( parent.getContext() );
            }
            else
            {
                view = ( TwitterRetweetView ) convertView;
            }
            view.update( retweets.get( position ) );
            return view;
        }
    }

    private class TwitterRetweetCallback implements Callback<List<Status>>
    {
        @Override
        public void success( List<Status> retweets, Response response )
        {
            // Subscriber : TwitterActionsFragment : onTwitterRetweetListDone()
            HashtaggerApp.bus.post( new TwitterRetweetListEvent( retweets, true ) );
        }

        @Override
        public void failure( RetrofitError retrofitError )
        {
            // Subscriber : TwitterActionsFragment : onTwitterRetweetListDone()
            HashtaggerApp.bus.post( new TwitterRetweetListEvent( null, false ) );
        }
    }
}
