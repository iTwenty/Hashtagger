package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.events.TwitterSearchDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.SearchResult;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.utils.Linkifier;

/**
 * Created by itwenty on 3/13/14.
 */
public class TwitterSearchHandler extends SitesSearchHandler
{
    public TwitterSearchHandler( SitesSearchListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return TwitterService.class;
    }

    @Override
    public boolean isSearchRunning()
    {
        return TwitterService.isIsSearchRunning();
    }

    @Subscribe
    public void onTwitterSearchDone( final TwitterSearchDoneEvent event )
    {
        final int searchType = event.getSearchType();
        if ( !event.isSuccess() )
        {
            getMainHandler().post( new Runnable()
            {
                @Override
                public void run()
                {
                    sitesSearchListener.onError( searchType );
                }
            } );
            return;
        }
        final SearchResult result = event.getSearchResult();
        for ( Status status : result.getStatuses() )
        {
            status.setLinkedText( Linkifier.getLinkedTwitterText( status.getText() ) );
            if ( status.isRetweet() )
            {
                status.getRetweetedStatus().setLinkedText( Linkifier.getLinkedTwitterText( status.getRetweetedStatus().getText() ) );
            }
        }
        getMainHandler().post( new Runnable()
        {
            @Override
            public void run()
            {
                sitesSearchListener.afterSearching( searchType, result.getStatuses() );
            }
        } );
    }
}
