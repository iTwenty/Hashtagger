package net.thetranquilpsychonaut.hashtagger;

/**
 * Created by itwenty on 3/14/14.
 */
public interface SitesHandler
{
    public void beginSearch();
    public void switchToListeningMode();
    public boolean isInListeningMode();
    public void pauseSearch();
    public void resumeSearch();
    public void destroyCurrentSearch();
}
