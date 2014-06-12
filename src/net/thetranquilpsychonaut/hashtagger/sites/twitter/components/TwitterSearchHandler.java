package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
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
        return TwitterService.getIsServiceRunning();
    }

    @Override
    public void cancelCurrentSearch()
    {
        TwitterService.setIsServiceRunning( false );
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
                final SearchResult result = ( SearchResult ) intent.getSerializableExtra( Result.RESULT_DATA );
                for ( Status status : result.getStatuses() )
                {
                    status.setLinkedText( Linkifier.getLinkedTwitterText( status.getText() ) );
                    if ( status.isRetweet() )
                    {
                        status.getRetweetedStatus().setLinkedText( Linkifier.getLinkedTwitterText( status.getRetweetedStatus().getText() ) );
                    }
                }
                main.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        sitesSearchListener.afterSearching( searchType, result.getStatuses() );
                    }
                } );
            }
        } ).start();
    }

    @Override
    public String getSearchActionName()
    {
        return HashtaggerApp.TWITTER_SEARCH_ACTION;
    }
}
