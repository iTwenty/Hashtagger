package net.thetranquilpsychonaut.hashtagger.sites.facebook.components;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import facebook4j.Paging;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.enums.Result;
import net.thetranquilpsychonaut.hashtagger.enums.SearchType;
import net.thetranquilpsychonaut.hashtagger.sites.components.SitesSearchHandler;

import java.util.Iterator;
import java.util.List;

/**
 * Created by itwenty on 4/4/14.
 */
public class FacebookSearchHandler extends SitesSearchHandler
{

    /*
    * Facebook post search results are navigated using pages.
    */
    static Paging<Post> newestPage;
    static Paging<Post> oldestPage;


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
        return FacebookService.isSearchRunning();
    }

    @Override
    public void onReceive( Context context, final Intent intent )
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                final int searchType = intent.getIntExtra( SearchType.SEARCH_TYPE_KEY, -1 );
                int resultType = intent.getIntExtra( Result.RESULT_KEY, -1 );
                if ( resultType == Result.FAILURE )
                {
                    sitesSearchListener.onError( searchType );
                    return;
                }
                final List<Post> results = ( List<Post> ) intent.getSerializableExtra( Result.RESULT_DATA );
                Iterator<Post> iterator = results.iterator();
                while ( iterator.hasNext() )
                {
                    if ( TextUtils.isEmpty( iterator.next().getMessage() ) )
                    {
                        iterator.remove();
                    }
                }
                new Handler( Looper.getMainLooper() ).post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        sitesSearchListener.afterSearching( searchType, results );
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
