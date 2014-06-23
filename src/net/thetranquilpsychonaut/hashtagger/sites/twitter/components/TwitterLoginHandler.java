package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.enums.AuthType;
import net.thetranquilpsychonaut.hashtagger.events.TwitterAuthDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesLoginHandler;
import net.thetranquilpsychonaut.hashtagger.utils.AccountPrefs;
import org.scribe.model.Token;

/**
 * Created by itwenty on 3/15/14.
 */
public class TwitterLoginHandler extends SitesLoginHandler
{
    public TwitterLoginHandler( SitesLoginListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return TwitterService.class;
    }

    @Subscribe
    public void onTwitterAuthDone( TwitterAuthDoneEvent event )
    {
        if ( !event.isSuccess() )
        {
            listener.onError();
            return;
        }
        int authType = event.getAuthType();
        switch ( authType )
        {
            case AuthType.REQUEST:
                final Token requestToken = event.getToken();
                final String authorizationUrl = event.getAuthUrl();
                getMainHandler().post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listener.onObtainingReqToken( requestToken, authorizationUrl );
                    }
                } );
                break;
            case AuthType.ACCESS:
                Token accessToken = event.getToken();
                String userName = event.getUserName();
                AccountPrefs.addTwitterDetails( accessToken.getToken(), accessToken.getSecret(), userName );
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
}
