package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import com.google.api.services.plus.model.Activity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesMediaView;
import net.thetranquilpsychonaut.hashtagger.sites.ui.ViewImageActivity;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusMediaView extends SitesMediaView
{
    private Activity activity;

    public GPlusMediaView( Context context )
    {
        super( context );
    }

    public GPlusMediaView( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public GPlusMediaView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    public void showMediaThumbnail( Object result )
    {
        this.activity = ( Activity ) result;
        Picasso.with( getContext() )
                .load( activity.getObject().getAttachments().get( 0 ).getImage().getUrl() )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvMedia );
        if ( "video".equals( activity.getObject().getAttachments().get( 0 ).getObjectType() ) )
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
        this.activity = ( Activity ) result;
        Picasso.with( getContext() )
                .load( activity.getObject().getAttachments().get( 0 ).getImage().getUrl() )
                .error( R.drawable.drawable_image_loading )
                .into( imgvMedia, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        if ( "video".equals( activity.getObject().getAttachments().get( 0 ).getObjectType() ) )
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
        String objectType = activity.getObject().getAttachments().get( 0 ).getObjectType();
        if ( "photo".equals( objectType ) )
        {
            Intent i = new Intent( getContext(), ViewImageActivity.class );
            String imageUrl;
            if ( null != activity.getObject().getAttachments().get( 0 ).getFullImage() )
            {
                imageUrl = activity.getObject().getAttachments().get( 0 ).getFullImage().getUrl();
            }
            else
            {
                imageUrl = activity.getObject().getAttachments().get( 0 ).getImage().getUrl();
            }
            i.putExtra( ViewImageActivity.IMAGE_URL_KEY, imageUrl );
            getContext().startActivity( i );
        }
        else if ( "video".equals( objectType ) )
        {
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setData( Uri.parse( activity.getObject().getAttachments().get( 0 ).getUrl() ) );
            getContext().startActivity( i );
        }
    }
}
