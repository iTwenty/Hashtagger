package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.events.FacebookAuthDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.model.Token;

/**
 * Created by itwenty on 4/7/14.
 */
public class FacebookLoginHandler extends SitesLoginHandler
{
    public FacebookLoginHandler( SitesLoginListener listener )
    {
        super( listener );
    }

    @Subscribe
    public void onFacebookAuthDone( FacebookAuthDoneEvent event )
    {
        if ( !event.isSuccess() )
        {
            listener.onError();
            return;
        }
        Token accessToken = event.getToken();
        String userName = event.getUserName();
        AccountPrefs.addFacebookDetails( accessToken.getToken(), userName );
        getMainHandler().post( new Runnable()
        {
            @Override
            public void run()
            {
                listener.onUserLoggedIn();
            }
        } );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return FacebookService.class;
    }
}
