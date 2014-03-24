package net.thetranquilpsychonaut.hashtagger.ui.twitter;

import twitter4j.auth.RequestToken;

/**
 * Created by itwenty on 3/15/14.
 */
public interface TwitterAuthHandlerListener
{
    public void onError();
    public void whileObtainingReqToken();
    public void onObtainingReqToken( RequestToken requestToken );
    public void onUserLoggedIn();
}
