package net.thetranquilpsychonaut.hashtagger.sites.instagram.components;

import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.events.InstagramSearchDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Comment;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.SearchResult;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 6/24/14.
 */
public class InstagramSearchHandler extends SitesSearchHandler
{
    public InstagramSearchHandler( SitesSearchListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return InstagramService.class;
    }

    @Override
    public boolean isSearchRunning()
    {
        return InstagramService.isSearchRunning();
    }

    @Subscribe
    public void onInstagramSearchDone( InstagramSearchDoneEvent event )
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
        final SearchResult searchResult = event.getSearchResult();

        // We need to multiply UNIX timestamps by 1000L since java expects milliseconds
        // while Instagram returns seconds.
        for ( Media media : searchResult.getData() )
        {
            media.setCreatedTime( media.getCreatedTime() * 1000L );
            if ( null != media.getCaption() )
            {
                media.getCaption().setCreatedTime( media.getCaption().getCreatedTime() * 1000L );
            }
            if ( null != media.getComments() && !Helper.isNullOrEmpty( media.getComments().getData() ) )
            {
                for ( Comment comment : media.getComments().getData() )
                {
                    comment.setCreatedTime( comment.getCreatedTime() * 1000L );
                }
            }
        }

        getMainHandler().post( new Runnable()
        {
            @Override
            public void run()
            {
                sitesSearchListener.afterSearching( searchType, searchResult.getData() );
            }
        } );
    }
}
