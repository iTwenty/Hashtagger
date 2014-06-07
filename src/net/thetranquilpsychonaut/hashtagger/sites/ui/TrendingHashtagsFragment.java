package net.thetranquilpsychonaut.hashtagger.sites.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.events.TwitterTrendsEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.components.TwitterTrendsService;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itwenty on 6/5/14.
 */
public class TrendingHashtagsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener
{
    private static final String SPINNER_CHOICE_KEY = "sck";
    private static final String TRENDING_LIST_KEY  = "tl";

    protected TrendingHashtagsAdapter    trendingHashtagsAdapter;
    protected List<String>               trendingHashtags;
    protected ListView                   lvTrendingHashtags;
    protected TextView                   tvTrendingHashtagsEmpty;
    protected Spinner                    spTrendingChoice;
    protected ArrayAdapter<CharSequence> trendingChoiceAdapter;
    protected TwitterTrendsService       twitterTrendsService;
    protected boolean                    isBoundToTrendsService;
    protected TwitterTrendsConnection    twitterTrendsConnection;

    private boolean isSpinnerCreated         = false;
    private boolean isFragmentFreshlyCreated = false;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        trendingHashtags = null == savedInstanceState ?
                new ArrayList<String>() :
                savedInstanceState.getStringArrayList( TRENDING_LIST_KEY );
        isBoundToTrendsService = false;
        twitterTrendsConnection = new TwitterTrendsConnection();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.fragment_trending_hashtags, container, false );
        lvTrendingHashtags = ( ListView ) view.findViewById( R.id.lv_trending );
        tvTrendingHashtagsEmpty = ( TextView ) view.findViewById( R.id.tv_trending_empty );
        spTrendingChoice = ( Spinner ) view.findViewById( R.id.sp_trending_choice );
        trendingHashtagsAdapter = new TrendingHashtagsAdapter( container.getContext(), trendingHashtags );
        trendingChoiceAdapter = ArrayAdapter.createFromResource( container.getContext(), R.array.trendingSpinnerChoices, android.R.layout.simple_spinner_item );
        trendingChoiceAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spTrendingChoice.setAdapter( trendingChoiceAdapter );
        spTrendingChoice.setOnItemSelectedListener( this );
        spTrendingChoice.setSelection( null == savedInstanceState ? 0 : savedInstanceState.getInt( SPINNER_CHOICE_KEY ), false );
        lvTrendingHashtags.setEmptyView( tvTrendingHashtagsEmpty );
        lvTrendingHashtags.setAdapter( trendingHashtagsAdapter );
        lvTrendingHashtags.setOnItemClickListener( this );
        isFragmentFreshlyCreated = null == savedInstanceState;
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        HashtaggerApp.bus.register( this );
        Intent i = new Intent( getActivity(), TwitterTrendsService.class );
        getActivity().bindService( i, twitterTrendsConnection, Context.BIND_AUTO_CREATE );
    }

    @Override
    public void onStop()
    {
        super.onStop();
        HashtaggerApp.bus.unregister( this );
        if ( isBoundToTrendsService )
        {
            getActivity().unbindService( twitterTrendsConnection );
            isBoundToTrendsService = false;
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putInt( SPINNER_CHOICE_KEY, spTrendingChoice.getSelectedItemPosition() );
        outState.putStringArrayList( TRENDING_LIST_KEY, ( java.util.ArrayList<String> ) trendingHashtags );
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id )
    {
        if ( parent.equals( lvTrendingHashtags ) )
        {
            ( ( NavDrawerActivity ) getActivity() ).dlNavDrawer.closeDrawers();
            String selectedHashtag = ( String ) parent.getItemAtPosition( position );
            Intent intent = new Intent( Intent.ACTION_SEARCH );
            intent.putExtra( SearchManager.QUERY, selectedHashtag );
            intent.setComponent( new ComponentName( parent.getContext(), SitesActivity.class ) );
            parent.getContext().startActivity( intent );
        }
    }

    @Subscribe
    public void onTwitterTrendsFound( TwitterTrendsEvent event )
    {
        int code = event.getStatus();
        if ( code == TwitterTrendsService.TRENDS_FOUND )
        {
            List<String> trends = event.getTrends();
            trendingHashtags.clear();
            trendingHashtags.addAll( trends );
            trendingHashtagsAdapter.notifyDataSetChanged();
        }
        else if ( code == TwitterTrendsService.TRENDS_NOT_AVAILABLE )
        {
            tvTrendingHashtagsEmpty.setText( "Trends not available" );
        }
        else if ( code == TwitterTrendsService.TWITTER_NOT_LOGGED_IN )
        {
            tvTrendingHashtagsEmpty.setText( "Log in to Twitter to see trending topics" );
        }
    }

    @Override
    public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
    {
        if ( parent.equals( spTrendingChoice ) && isSpinnerCreated )
        {
            switch ( position )
            {
                case 1:
                    twitterTrendsService.fetchTrends( TwitterTrendsService.GLOBAL );
                    break;
                case 0: // fall through
                default:
                    twitterTrendsService.fetchTrends( TwitterTrendsService.LOCAL );
                    break;
            }
        }
        else
        {
            isSpinnerCreated = true;
        }
    }

    @Override
    public void onNothingSelected( AdapterView<?> parent )
    {

    }

    private class TwitterTrendsConnection implements ServiceConnection
    {
        @Override
        public void onServiceConnected( ComponentName name, IBinder service )
        {
            Helper.debug( "onServiceConnected" );
            TwitterTrendsService.TwitterTrendsBinder binder = ( TwitterTrendsService.TwitterTrendsBinder ) service;
            twitterTrendsService = binder.getService();
            if ( isFragmentFreshlyCreated )
            {
                twitterTrendsService.fetchTrends( TwitterTrendsService.LOCAL );
            }
            isBoundToTrendsService = true;
        }

        @Override
        public void onServiceDisconnected( ComponentName name )
        {
            isBoundToTrendsService = false;
        }
    }
}
