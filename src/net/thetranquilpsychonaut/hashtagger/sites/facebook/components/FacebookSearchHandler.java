package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.text.TextUtils;
import com.squareup.otto.Subscribe;
import net.thetranquilpsychonaut.hashtagger.events.FacebookSearchDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.Post;
import net.thetranquilpsychonaut.hashtagger.sites.facebook.retrofit.pojos.SearchResult;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import net.thetranquilpsychonaut.hashtagger.utils.Linkifier;

import java.util.Iterator;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookSearchHandler extends SitesSearchHandler
{
    public FacebookSearchHandler( SitesSearchListener listener )
    {
        super( listener );
    }

    @Override
    protected Class<?> getServiceClass()
    {
        return FacebookService.class;
    }

    @Override
    public boolean isSearchRunning()
    {
        return FacebookService.getIsServiceRunning();
    }

    @Override
    public void cancelCurrentSearch()
    {
        FacebookService.setIsServiceRunning( false );
    }

    @Subscribe
    public void onFacebookSearchDone( final FacebookSearchDoneEvent event )
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
        Iterator<Post> iterator = searchResult.getData().iterator();
        while ( iterator.hasNext() )
        {
            Post post = iterator.next();
            if ( TextUtils.isEmpty( post.getMessage() ) )
            {
                Helper.debug( "Facebook post with empty message removed. ID is : " + post.getId() );
                iterator.remove();
            }
        }
        for ( Post post : searchResult.getData() )
        {
            post.setLinkedText( Linkifier.getLinkedFacebookText( post.getMessage() ) );
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
