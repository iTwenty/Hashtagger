package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class FacebookLinkRow extends SitesListRow implements View.OnClickListener
{
    private FacebookHeader facebookHeader;
    private TextView       tvMessage;
    private RelativeLayout rlLinkWrapper;
    private ImageView      imgvPicture;
    private ImageView      imgvPlayButton;
    private TextView       tvName;
    private TextView       tvDescription;
    private TextView       tvCaption;
    private Post           post;

    protected FacebookLinkRow( Context context )
    {
        super( context );
    }

    protected FacebookLinkRow( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    protected FacebookLinkRow( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void init( Context context )
    {
        inflate( context, R.layout.facebook_link_row, this );
        facebookHeader = ( FacebookHeader ) findViewById( R.id.facebook_header );
        tvMessage = ( TextView ) findViewById( R.id.tv_message );
        rlLinkWrapper = ( RelativeLayout ) findViewById( R.id.rl_link_wrapper );
        imgvPicture = ( ImageView ) findViewById( R.id.imgv_picture );
        imgvPlayButton = ( ImageView ) findViewById( R.id.imgv_play_button );
        tvName = ( TextView ) findViewById( R.id.tv_name );
        tvDescription = ( TextView ) findViewById( R.id.tv_description );
        tvCaption = ( TextView ) findViewById( R.id.tv_caption );
        rlLinkWrapper.setOnClickListener( this );
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
        if ( null == post.getPicture() )
        {
            imgvPicture.setImageDrawable( getResources().getDrawable( R.drawable.drawable_image_loading ) );
        }
        else
        {
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
        tvName.setText( post.getName() );
        tvDescription.setText( post.getDescription() );
        tvCaption.setText( post.getCaption() );
        if ( null == post.getLink() )
        {
            rlLinkWrapper.setVisibility( GONE );
        }
        else
        {
            rlLinkWrapper.setVisibility( VISIBLE );
        }
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( rlLinkWrapper ) )
        {
            if ( null == post.getLink() )
            {
                return;
            }
            Intent intent = new Intent( Intent.ACTION_VIEW );
            intent.setData( Uri.parse( post.getLink().toString() ) );
            getContext().startActivity( intent );
        }
    }
}
