package net.thetranquilpsychonaut.hashtagger.sites.instagram.components;

import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.events.InstagramAuthDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.model.Token;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramLoginHandler extends SitesLoginHandler
{
    public InstagramLoginHandler( SitesLoginListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return InstagramService.class;
    }

    @Subscribe
    public void onInstagramAuthDone( InstagramAuthDoneEvent event )
    {
        if ( !event.isSuccess() )
        {
            listener.onError();
            return;
        }
        Token accessToken = event.getToken();
        String userName = event.getUserName();
        AccountPrefs.addInstagramDetails( accessToken.getToken(), userName );
        getMainHandler().post( new Runnable()
        {
            @Override
            public void run()
            {
                listener.onUserLoggedIn();
            }
        } );
    }
}
