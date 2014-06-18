package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
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

    @Override
    public void onReceive( Context context, final Intent intent )
    {
        final Handler main = new Handler( Looper.getMainLooper() );
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                final int searchType = intent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
                int resultType = intent.getIntExtra( Result.RESULT_KEY, -1 );
                if ( resultType == Result.FAILURE )
                {
                    main.post( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            sitesSearchListener.onError( searchType );
                        }
                    } );
                    return;
                }
                final SearchResult searchResult = ( SearchResult ) intent.getSerializableExtra( Result.RESULT_DATA );
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
                main.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        sitesSearchListener.afterSearching( searchType, searchResult.getData() );
                    }
                } );
            }
        } ).start();
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.FACEBOOK_SEARCH_ACTION;
    }
}
