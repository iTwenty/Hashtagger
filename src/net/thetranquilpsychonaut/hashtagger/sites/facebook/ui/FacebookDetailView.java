package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.HashtaggerApp;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 4/18/14.
 */
public class FacebookDetailView extends RelativeLayout
{
    private ImageView imgvPicture;
    private TextView  tvName;
    private TextView  tvDescription;
    private TextView  tvCaption;

    public FacebookDetailView( Context context )
    {
        this( context, null, 0 );
    }

    public FacebookDetailView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public FacebookDetailView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_detail_view, this );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        tvName = ( TextView ) findViewById( R.id.tv_name );
        tvDescription = ( TextView ) findViewById( R.id.tv_description );
        tvCaption = ( TextView ) findViewById( R.id.tv_caption );
    }

    public void showDetailsFromPost( final Post post )
    {
        if ( null == post.getPicture() )
        {
            imgvPicture.setImageDrawable( getResources().getDrawable( R.drawable.drawable_image_loading ) );
        }
        else
        {
            UrlImageViewHelper.setUrlDrawable( imgvPicture, post.getPicture().toString(), getResources().getDrawable( R.drawable.drawable_image_loading ), HashtaggerApp.CACHE_DURATION_MS );
        }
        tvName.setText( post.getName() );
        tvDescription.setText( post.getDescription() );
        tvCaption.setText( post.getCaption() );
        this.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( post.getLink().toString() ) );
                getContext().startActivity( intent );
            }
        } );
    }

    public void clearView()
    {
        imgvPicture.setImageDrawable( null );
        tvName.setText( "" );
        tvDescription.setText( "" );
        tvCaption.setText( "" );
        this.setOnClickListener( null );
    }
}
