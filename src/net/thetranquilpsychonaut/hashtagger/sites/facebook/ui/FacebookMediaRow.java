package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesButtons;
import net.thetranquilpsychonaut.hashtagger.sites.ui.SitesListRow;

/**
 * Created by itwenty on 5/2/14.
 */
public class FacebookMediaRow extends SitesListRow implements View.OnClickListener
{
    private FacebookHeader facebookHeader;
    private TextView       tvMessage;
    private ImageView      imgvPicture;
    private ImageView      imgvPlayButton;
    private Post           post;

    protected FacebookMediaRow( Context context )
    {
        super( context );
    }

    protected FacebookMediaRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookMediaRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_media_row, this );
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        imgvPlayButton = ( ImageView ) findViewById( R.id.imgv_play_button );
        imgvPicture.setOnClickListener( this );
        super.init( context );
    }

    @Override
    protected SitesButtons initSitesButtons()
    {
        return ( SitesButtons ) findViewById( R.id.facebook_buttons );
    }

    @Override
    public void updateRow( Object result )
    {
        this.post = ( Post ) result;
        facebookHeader.updateHeader( post );
        tvMessage.setText( post.getMessage() );
        Picasso.with( getContext() )
                .load( post.getPicture().toString() )
                .error( R.drawable.drawable_image_loading )
                .into( imgvPicture, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        imgvPlayButton.setVisibility( "video".equals( post.getType() ) ? VISIBLE : GONE );
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
        if ( v.equals( imgvPicture ) )
        {
            if ( "video".equals( post.getType() ) )
            {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( post.getSource().toString() ) );
                getContext().startActivity( intent );
            }
            else if ( "photo".equals( post.getType() ) )
            {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( post.getPicture().toString().replace( "_s.", "_o." ) ) );
                getContext().startActivity( intent );
            }
        }
    }
}
