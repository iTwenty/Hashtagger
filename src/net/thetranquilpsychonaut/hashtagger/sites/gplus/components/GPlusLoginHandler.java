package net.thetranquilpsychonaut.hashtagger.sites.gplus.components;

import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.events.GPlusAuthDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import org.scribe.model.Token;

/**
 * Created by itwenty on 5/6/14.
 */
public class GPlusLoginHandler extends SitesLoginHandler
{
    public GPlusLoginHandler( SitesLoginListener listener )
    {
        super( listener );
    }

    @Subscribe
    public void onGPlusAuthDone( GPlusAuthDoneEvent event )
    {
        if ( !event.isSuccess() )
        {
            listener.onError();
            return;
        }
        Token accessToken = event.getToken();
        String userName = event.getUserName();
        String refreshToken = Helper.extractJsonStringField( accessToken.getRawResponse(), "refresh_token" );
        AccountPrefs.addGPlusDetails( accessToken.getToken(), refreshToken, userName );
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
        return GPlusService.class;
    }
}
