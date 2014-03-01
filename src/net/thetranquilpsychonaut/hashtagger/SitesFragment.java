package net.thetranquilpsychonaut.hashtagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ViewAnimator;
import com.squareup.otto.Subscribe;

/**
 * Created by itwenty on 2/26/14.
 */
public abstract class SitesFragment extends Fragment implements ConnectivityChangeListener
{
    public enum ActiveView
    {
        READY, LOADING, NO_NETWORK, LOGIN
    }

    ;

    ViewAnimator vaPossibleViews;
    ActiveView   activeView;
    View         viewReady;
    View         viewLoading;
    View         viewNoNetwork;
    View         viewLogin;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        HashtaggerApp.bus.register( this );
        View v = inflater.inflate( R.layout.fragment_sites, container, false );
        vaPossibleViews = ( ViewAnimator ) v.findViewById( R.id.va_possible_views );
        viewReady = getViewReady( inflater );
        viewLoading = getViewLoading( inflater );
        viewNoNetwork = getViewNoNetwork( inflater );
        viewLogin = getViewLogin( inflater );
        vaPossibleViews.addView( viewReady, 0 );
        vaPossibleViews.addView( viewLoading, 1 );
        vaPossibleViews.addView( viewNoNetwork, 2 );
        vaPossibleViews.addView( viewLogin, 3 );
        return v;
    }

    protected abstract View getViewReady( LayoutInflater inflater );

    protected abstract View getViewLoading( LayoutInflater inflater );

    protected abstract View getViewNoNetwork( LayoutInflater inflater );

    protected abstract View getViewLogin( LayoutInflater inflater );

    @Subscribe
    protected abstract void searchHashtag( String hashtag );

    @Override
    public void onResume()
    {
        ( ( MainActivity ) getActivity() ).getConnectivityChangeReceiver().addListener( this );
        super.onResume();
    }

    @Override
    public void onConnected()
    {
        if ( activeView != ActiveView.READY )
        {
            vaPossibleViews.setDisplayedChild( vaPossibleViews.indexOfChild( viewReady ) );
            activeView = ActiveView.READY;
        }
    }

    @Override
    public void onDisconnected()
    {
        if ( activeView != ActiveView.NO_NETWORK )
        {
            vaPossibleViews.setDisplayedChild( vaPossibleViews.indexOfChild( viewNoNetwork ) );
            activeView = ActiveView.NO_NETWORK;
        }
    }
}
