package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.TwitterRetrofitService;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by itwenty on 5/11/14.
 */
public class TwitterAction
{
    public void executeReplyAction( String reply, String inReplyToUserId )
    {
        TwitterRetrofitService.api().replyToStatus( reply, inReplyToUserId, new Callback<Status>()
        {
            @Override
            public void success( Status status, Response response )
            {
                HashtaggerApp.bus.post( new TwitterReplyEvent( true ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                HashtaggerApp.bus.post( new TwitterReplyEvent( false ) );
            }
        } );
    }

    public void executeRetweetAction( String retweetId, final int position )
    {
        TwitterRetrofitService.api().retweetStatus( retweetId, new Callback<Status>()
        {
            @Override
            public void success( Status status, Response response )
            {
                HashtaggerApp.bus.post( new TwitterRetweetEvent( true, position, status ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                HashtaggerApp.bus.post( new TwitterRetweetEvent( false, position, null ) );
            }
        } );
    }

    public void executeFavoriteAction( String statusId, boolean isFavorited, final int position )
    {
        Callback<Status> cb = new Callback<Status>()
        {
            @Override
            public void success( Status status, Response response )
            {
                HashtaggerApp.bus.post( new TwitterFavoriteEvent( true, position, status ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                HashtaggerApp.bus.post( new TwitterFavoriteEvent( false, position, null ) );
            }
        };

        if ( isFavorited )
        {
            TwitterRetrofitService.api().destroyFavorite( statusId, cb );
        }
        else
        {
            TwitterRetrofitService.api().createFavorite( statusId, cb );
        }
    }
}
