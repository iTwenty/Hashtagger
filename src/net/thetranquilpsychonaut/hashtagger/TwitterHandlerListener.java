package net.thetranquilpsychonaut.hashtagger;

import twitter4j.Status;

import java.util.List;

/**
 * Created by itwenty on 3/13/14.
 */
public interface TwitterHandlerListener
{
    public void onPreExecute();

    public void onPostExecute( List<Status> statuses );

    public void onSwitchToListener();

    public void onStatus( Status status );

    public void onError();
}
