package net.thetranquilpsychonaut.hashtagger.sites.instagram.ui;

import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.events.InstagramLikeDoneEvent;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.Instagram;
import net.thetranquilpsychonaut.hashtagger.sites.instagram.retrofit.pojos.Media;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by itwenty on 7/2/14.
 */
public class InstagramActions
{
    public static void executeLikeAction( final Media media )
    {
        Callback<Response> cb = new Callback<Response>()
        {
            @Override
            public void success( Response response, Response response2 )
            {
                if ( media.isUserHasLiked() )
                {
                    media.setUserHasLiked( false );
                    media.getLikes().setCount( media.getLikes().getCount() - 1 );
                }
                else
                {
                    media.setUserHasLiked( true );
                    media.getLikes().setCount( media.getLikes().getCount() + 1 );
                }

                HashtaggerApp.bus.post( new InstagramLikeDoneEvent( true, media ) );
            }

            @Override
            public void failure( RetrofitError retrofitError )
            {
                HashtaggerApp.bus.post( new InstagramLikeDoneEvent( false, media ) );
            }
        };

        if ( media.isUserHasLiked() )
        {
            Instagram.api().deleteLike( media.getId(), cb );
        }
        else
        {
            Instagram.api().postLike( media.getId(), cb );
        }
    }
}
