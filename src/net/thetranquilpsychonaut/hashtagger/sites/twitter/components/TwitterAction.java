package net.thetranquilpsychonaut.hashtagger.sites.twitter.components;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.events.TwitterFavoriteDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterReplyDoneEvent;
import net.thetranquilpsychonaut.hashtagger.events.TwitterRetweetDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.Twitter;
import net.thetranquilpsychonaut.hashtagger.sites.twitter.retrofit.pojos.Status;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by itwenty on 5/11/14.
 */
public class TwitterAction
{
    public void executeReplyAction( String reply, final Status status )
    {
        Twitter.api().replyToStatus( reply, status.getInReplyToStatusIdStr(), new Callback<Status>()
        {
            @Override
            public void success( Status newStatus, Response response )
            {
                // Subscriber : TwitterFragment : onReplyDone()
                HashtaggerApp.bus.post( new TwitterReplyDoneEvent( true, status ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                // Subscriber : TwitterFragment : onReplyDone()
                HashtaggerApp.bus.post( new TwitterReplyDoneEvent( false, status ) );
            }
        } );
    }

    public void executeRetweetAction( final Status status )
    {
        Twitter.api().retweetStatus( status.getIdStr(), new Callback<Status>()
        {
            @Override
            public void success( Status newStatus, Response response )
            {
                status.setRetweeted( newStatus.isRetweeted() );
                status.setRetweetCount( newStatus.getRetweetCount() );
                // Subscriber : TwitterFragment : onRetweetDone()
                HashtaggerApp.bus.post( new TwitterRetweetDoneEvent( true, status ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                // Subscriber : TwitterFragment : onRetweetDone()
                HashtaggerApp.bus.post( new TwitterRetweetDoneEvent( false, status ) );
            }
        } );
    }

    public void executeFavoriteAction( final Status status )
    {
        Callback<Status> cb = new Callback<Status>()
        {
            @Override
            public void success( Status newStatus, Response response )
            {
                status.setFavorited( newStatus.isFavorited() );
                status.setFavoriteCount( newStatus.getFavoriteCount() );
                // Subscriber : TwitterFragment : onFavoriteDone()
                HashtaggerApp.bus.post( new TwitterFavoriteDoneEvent( true, status ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                // Subscriber : TwitterFragment : onFavoriteDone()
                HashtaggerApp.bus.post( new TwitterFavoriteDoneEvent( false, status ) );
            }
        };

        if ( status.isFavorited() )
        {
            Twitter.api().destroyFavorite( status.getIdStr(), cb );
        }
        else
        {
            Twitter.api().createFavorite( status.getIdStr(), cb );
        }
    }
}
