package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesMediaView;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;
import net.thetranquilpsychonaut.hashtagger.utils.Helper;

/**
 * Created by itwenty on 5/17/14.
 */
public class FacebookMediaView extends SitesMediaView
{
    private Post post;

    public FacebookMediaView( Context context )
    {
        super( context );
    }

    public FacebookMediaView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public FacebookMediaView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    public void showMediaThumbnail( Object result )
    {
        this.post = ( Post ) result;
        Picasso.with( getContext() )
                .load( post.getPicture().toString() )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvMedia );
        if ( "video".equals( post.getType() ) )
        {
            imgvPlay.setVisibility( VISIBLE );
        }
        else
        {
            imgvPlay.setVisibility( GONE );
        }
    }

    @Override
    public void showMedia( Object result )
    {
        this.post = ( Post ) result;
        Picasso.with( getContext() )
                .load( Helper.getFacebookLargeImageUrl( post.getPicture().toString() ) )
                .error( R.drawable.drawable_image_loading )
                .into( imgvMedia, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        if ( "video".equals( post.getType() ) )
                        {
                            imgvPlay.setVisibility( VISIBLE );
                        }
                        else
                        {
                            imgvPlay.setVisibility( GONE );
                        }
                    }

                    @Override
                    public void onError()
                    {

                    }
                } );
    }

    @Override
    public void onClick( View v )
    {
        if ( "video".equals( post.getType() ) )
        {
            Intent intent = new Intent( Intent.ACTION_VIEW );
            intent.setData( Uri.parse( post.getSource().toString() ) );
            getContext().startActivity( intent );
        }
        else if ( "photo".equals( post.getType() ) )
        {
            Intent intent = new Intent( getContext(), ViewImageActivity.class );
            intent.putExtra( ViewImageActivity.IMAGE_URL_KEY, Helper.getFacebookLargeImageUrl( post.getPicture().toString() ) );
            getContext().startActivity( intent );
        }
    }
}
