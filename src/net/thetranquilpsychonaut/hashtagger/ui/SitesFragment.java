package net.thetranquilpsychonaut.hashtagger.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.ConnectivityChangeListener;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.exception.NoNetworkException;
import net.thetranquilpsychonaut.hashtagger.exception.NotLoggedInException;
import net.thetranquilpsychonaut.hashtagger.otto.HashtagEvent;

/**
 * Created by itwenty on 2/26/14.
 */
public abstract class SitesFragment extends Fragment implements ConnectivityChangeListener
{
    protected static enum Views
    {
        READY( 0 ), LOADING( 1 ), NO_NETWORK( 2 ), LOGIN( 3 ), ERROR( 4 );
        private int index;

        Views( int index )
        {
            this.index = index;
        }

        public int getIndex()
        {
            return index;
        }
    }

    private   ViewAnimator     vaPossibleViews;
    protected View             viewReady;
    protected View             viewLoading;
    protected View             viewNoNetwork;
    protected View             viewLogin;
    protected View             viewError;
    protected View             lastActiveView;
    protected SitesHandler     sitesHandler;
    protected SitesUserHandler sitesUserHandler;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        HashtaggerApp.bus.register( this );
        onInitialize();
        sitesUserHandler = getSitesUserHandler();
        sitesHandler = getSitesSearchHandler();
        onHandlersCreated();
        View v = inflater.inflate( R.layout.fragment_sites, container, false );
        vaPossibleViews = ( ViewAnimator ) v.findViewById( R.id.va_possible_views );
        viewReady = fetchView( Views.READY, inflater );
        viewLoading = fetchView( Views.LOADING, inflater );
        viewNoNetwork = fetchView( Views.NO_NETWORK, inflater );
        viewLogin = fetchView( Views.LOGIN, inflater );
        viewError = fetchView( Views.ERROR, inflater );
        vaPossibleViews.addView( viewReady, Views.READY.getIndex() );
        vaPossibleViews.addView( viewLoading, Views.LOADING.getIndex() );
        vaPossibleViews.addView( viewNoNetwork, Views.NO_NETWORK.getIndex() );
        vaPossibleViews.addView( viewLogin, Views.LOGIN.getIndex() );
        vaPossibleViews.addView( viewError, Views.ERROR.getIndex() );
        onViewsCreated();
        lastActiveView = vaPossibleViews.getCurrentView();
        return v;
    }

    protected void onInitialize() {}

    protected abstract SitesUserHandler getSitesUserHandler();

    protected abstract SitesHandler getSitesSearchHandler();

    protected void onHandlersCreated() {}

    protected abstract View fetchView( Views views, LayoutInflater inflater );

    protected void onViewsCreated() {}

    @Subscribe
    protected abstract void searchHashtag( HashtagEvent event );

    protected void ensureNetworkConnected() throws NoNetworkException
    {
        if( !HashtaggerApp.isNetworkConnected() )
            throw new NoNetworkException( "Please connect to a network first." );
    }

    protected abstract void ensureUserLoggedIn() throws NotLoggedInException;

    @Override
    public void onPause()
    {
        if( sitesHandler.isInListeningMode() )
            sitesHandler.pauseSearch();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        if ( sitesHandler.isInListeningMode() )
            sitesHandler.resumeSearch();
        ( ( MainActivity ) getActivity() ).getConnectivityChangeReceiver().addListener( this );
        super.onResume();
    }

    public void showView( View view )
    {
        View lastActiveView = vaPossibleViews.getCurrentView();
        int viewIndex = vaPossibleViews.indexOfChild( view );
        if ( viewIndex == -1 )
            throw new RuntimeException( "showView() must be called with a view contained in the ViewAnimator" );
        vaPossibleViews.setDisplayedChild( viewIndex );
        this.lastActiveView = lastActiveView;
    }

    @Override
    public void onConnected()
    {
        if ( sitesHandler.isInListeningMode() )
            sitesHandler.resumeSearch();
        else
            showView( lastActiveView );
    }

    @Override
    public void onDisconnected()
    {
        if ( sitesHandler.isInListeningMode() )
            sitesHandler.pauseSearch();
        else
            showView( viewNoNetwork );
    }

    public View getCurrentView()
    {
        return vaPossibleViews.getCurrentView();
    }
}