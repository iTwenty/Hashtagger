package net.thetranquilpsychonaut.hashtagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;
import com.squareup.otto.Subscribe;

/**
 * Created by itwenty on 2/26/14.
 */
public abstract class SitesFragment extends Fragment implements ConnectivityChangeListener
{
    public enum ActiveView
    {
        READY       (0),
        LOADING     (1),
        NO_NETWORK  (2),
        LOGIN       (3),
        ERROR       (4);

        private int index;

        ActiveView( int index )
        {
            this.index = index;
        }

        int index()
        {
            return index;
        }
    }

    ;

    ViewAnimator vaPossibleViews;
    ActiveView   activeView;
    View         viewReady;
    View         viewLoading;
    View         viewNoNetwork;
    View         viewLogin;
    View         viewError;
    SitesHandler sitesHandler;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        HashtaggerApp.bus.register( this );
        View v = inflater.inflate( R.layout.fragment_sites, container, false );
        vaPossibleViews = ( ViewAnimator ) v.findViewById( R.id.va_possible_views );
        sitesHandler = getSitesHandler();
        viewReady = getViewReady( inflater );
        viewLoading = getViewLoading( inflater );
        viewNoNetwork = getViewNoNetwork( inflater );
        viewLogin = getViewLogin( inflater );
        viewError = getViewError( inflater );
        vaPossibleViews.addView( viewReady, ActiveView.READY.index() );
        vaPossibleViews.addView( viewLoading, ActiveView.LOADING.index() );
        vaPossibleViews.addView( viewNoNetwork, ActiveView.NO_NETWORK.index() );
        vaPossibleViews.addView( viewLogin, ActiveView.LOGIN.index() );
        vaPossibleViews.addView( viewError, ActiveView.ERROR.index() );
        return v;
    }

    protected abstract SitesHandler getSitesHandler();

    protected abstract View getViewReady( LayoutInflater inflater );

    protected abstract View getViewLoading( LayoutInflater inflater );

    protected abstract View getViewNoNetwork( LayoutInflater inflater );

    protected abstract View getViewLogin( LayoutInflater inflater );

    protected abstract View getViewError( LayoutInflater inflater );

    @Subscribe
    protected abstract void searchHashtag( String hashtag );

    @Override
    public void onResume()
    {
        ( ( MainActivity ) getActivity() ).getConnectivityChangeReceiver().addListener( this );
        super.onResume();
    }

    public void showReadyView()
    {
        vaPossibleViews.setDisplayedChild( vaPossibleViews.indexOfChild( viewReady ) );
        activeView = ActiveView.READY;
    }

    public void showLoadingView()
    {
        vaPossibleViews.setDisplayedChild( vaPossibleViews.indexOfChild( viewLoading ) );
        activeView = ActiveView.LOADING;
    }

    public void showNoNetworkView()
    {
        vaPossibleViews.setDisplayedChild( vaPossibleViews.indexOfChild( viewNoNetwork ) );
        activeView = ActiveView.NO_NETWORK;
    }

    public void showLoginView()
    {
        vaPossibleViews.setDisplayedChild( vaPossibleViews.indexOfChild( viewLogin ) );
        activeView = ActiveView.LOGIN;
    }

    public void showErrorView()
    {
        vaPossibleViews.setDisplayedChild( vaPossibleViews.indexOfChild( viewError ) );
        activeView = ActiveView.ERROR;
    }

    @Override
    public void onConnected()
    {
        if( sitesHandler.isInListeningMode() )
            sitesHandler.resumeSearch();
        else
            showReadyView();
    }

    @Override
    public void onDisconnected()
    {
        if( sitesHandler.isInListeningMode() )
            sitesHandler.pauseSearch();
        else
            showNoNetworkView();
    }
}
