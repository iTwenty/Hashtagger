package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.Twitter;
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
        Twitter.api().replyToStatus( reply, inReplyToUserId, new Callback<Status>()
        {
            @Override
            public void success( Status status, Response response )
            {
                // Subscriber : TwitterFragment : onReplyDone()
                HashtaggerApp.bus.post( new TwitterReplyEvent( true ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                // Subscriber : TwitterFragment : onReplyDone()
                HashtaggerApp.bus.post( new TwitterReplyEvent( false ) );
            }
        } );
    }

    public void executeRetweetAction( String retweetId, final int position )
    {
        Twitter.api().retweetStatus( retweetId, new Callback<Status>()
        {
            @Override
            public void success( Status status, Response response )
            {
                // Subscriber : TwitterFragment : onRetweetDone()
                HashtaggerApp.bus.post( new TwitterRetweetEvent( true, position ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                // Subscriber : TwitterFragment : onRetweetDone()
                HashtaggerApp.bus.post( new TwitterRetweetEvent( false, position ) );
            }
        } );
    }

    public void executeFavoriteAction( String statusId, final boolean isFavorited, final int position )
    {
        Callback<Status> cb = new Callback<Status>()
        {
            @Override
            public void success( Status status, Response response )
            {
                // Subscriber : TwitterFragment : onFavoriteDone()
                HashtaggerApp.bus.post( new TwitterFavoriteEvent( true, position, isFavorited ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                // Subscriber : TwitterFragment : onFavoriteDone()
                HashtaggerApp.bus.post( new TwitterFavoriteEvent( false, position, isFavorited ) );
            }
        };

        if ( isFavorited )
        {
            Twitter.api().destroyFavorite( statusId, cb );
        }
        else
        {
            Twitter.api().createFavorite( statusId, cb );
        }
    }
}
