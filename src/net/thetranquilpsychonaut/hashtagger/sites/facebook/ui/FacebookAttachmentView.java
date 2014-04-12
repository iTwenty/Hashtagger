package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.AttributeSet;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.Helper;
import net.thetranquilpsychonaut.hashtagger.R;

import java.net.URISyntaxException;

/**
 * Created by itwenty on 4/12/14.
 */
public class FacebookAttachmentView extends RelativeLayout
{
    private ImageView imgvPictureCenter;
    private ImageView imgvPicture;
    private TextView  tvName;
    private TextView  tvDescription;
    private TextView  tvCaption;

    public FacebookAttachmentView( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookAttachmentView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookAttachmentView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        LayoutInflater.from( context ).inflate( R.layout.view_facebook_attachment, this );
        imgvPictureCenter = ( ImageView ) findViewById( R.id.imgv_picture_center );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        tvName = ( TextView ) findViewById( R.id.tv_name );
        tvDescription = ( TextView ) findViewById( R.id.tv_description );
        tvCaption = ( TextView ) findViewById( R.id.tv_caption );
        // We set the center visibility to gone to avoid overlap issues, just in case! =]
        imgvPictureCenter.setVisibility( GONE );
    }

    public void setAttachmentFromPost( final Post post )
    {
        // No need to show anything if post is of type status or if we have object id but no picture
        if( ( "status".equals( post.getType() ) ) ||
            ( null != post.getObjectId() && null == post.getPicture() ) )
        {
            this.setVisibility( GONE );
            return;
        }
        // If object id is not null and we have a picture, show it in center
        if( null != post.getObjectId() )
        {
            imgvPicture.setVisibility( GONE );
            tvName.setVisibility( GONE );
            tvDescription.setVisibility( GONE );
            tvCaption.setVisibility( GONE );
            imgvPictureCenter.setVisibility( VISIBLE );
            UrlImageViewHelper.setUrlDrawable( imgvPictureCenter, post.getPicture().toString(), getResources().getDrawable( R.drawable.drawable_image_loading ) );
        }
        // Else we have related info present. Show the info view
        else
        {
            imgvPicture.setVisibility( VISIBLE );
            tvName.setVisibility( VISIBLE );
            tvDescription.setVisibility( VISIBLE );
            tvCaption.setVisibility( VISIBLE );
            imgvPictureCenter.setVisibility( GONE );
            if( null != post.getPicture() )
                UrlImageViewHelper.setUrlDrawable( imgvPicture, post.getPicture().toString(), getResources().getDrawable( R.drawable.drawable_image_loading ) );
            else
                imgvPicture.setImageDrawable( getResources().getDrawable( R.drawable.drawable_image_loading ) );
            tvName.setText( post.getName() );
            tvDescription.setText( post.getDescription() );
            tvCaption.setText( post.getCaption() );
            this.setOnClickListener( new OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                    Intent i = new Intent( Intent.ACTION_VIEW );
                    i.setData( Uri.parse( post.getLink().toString() ) );
                    getContext().startActivity( i );
                }
            } );
        }
    }
}
