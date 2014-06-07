package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;
import twitter4j.QueryResult;

/**
 * Created by itwenty on 3/13/14.
 */
public class TwitterSearchHandler extends SitesSearchHandler
{
    /*
    max and since ids are used to navigate through the tweets timeline.
    tweet ids are time based i.e later tweets have higher ids than older tweets.
    */

    static long maxId;
    static long sinceId;

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
                final QueryResult result = ( QueryResult ) intent.getSerializableExtra( Result.RESULT_DATA );
                main.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        sitesSearchListener.afterSearching( searchType, result.getTweets() );
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
