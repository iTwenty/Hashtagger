package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Activity;
import net.thetranquilpsychonaut.hashtagger.sites.gplus.retrofit.pojos.Attachment;
import net.thetranquilpsychonaut.hashtagger.widgets.VideoThumbnail;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusDetailView extends RelativeLayout implements Callback, View.OnClickListener
{
    private VideoThumbnail imgvThumbnail;
    private TextView       tvTitle;
    private TextView       tvDescription;
    private Attachment     attachment;

    public GPlusDetailView( Context context )
    {
        this( context, null, R.attr.GPlusDetailViewStyle );
    }

    public GPlusDetailView( Context context, AttributeSet attrs )
    {
        this( context, attrs, R.attr.GPlusDetailViewStyle );
    }

    public GPlusDetailView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.gplus_detail_view, this );
        imgvThumbnail = ( VideoThumbnail ) findViewById( R.id.imgv_thumbnail );
        tvTitle = ( TextView ) findViewById( R.id.tv_title );
        tvDescription = ( TextView ) findViewById( R.id.tv_description );
        this.setOnClickListener( this );
    }

    public void showDetails( final Activity activity )
    {
        this.attachment = activity.getObject().getAttachments().get( 0 );
        if ( null != attachment.getImage() )
        {
            Picasso.with( getContext() )
                    .load( attachment.getImage().getUrl() )
                    .error( R.drawable.gplus_sketch )
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into( imgvThumbnail.getVideoThumbnail(), this );
        }
        else
        {
            Picasso.with( getContext() )
                    .load( R.drawable.gplus_sketch )
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into( imgvThumbnail.getVideoThumbnail(), this );
        }
        tvTitle.setText( attachment.getDisplayName() );
        if ( TextUtils.isEmpty( attachment.getContent() ) )
        {
            tvTitle.setMaxLines( 2 );
            tvDescription.setText( "" );
        }
        else
        {
            tvTitle.setMaxLines( 1 );
            tvDescription.setText( attachment.getContent() );
        }
    }

    @Override
    public void onSuccess()
    {
        if ( "video".equals( attachment.getObjectType() ) )
        {
            imgvThumbnail.showPlayButton( true );
        }
        else
        {
            imgvThumbnail.showPlayButton( false );
        }
    }

    @Override
    public void onError()
    {

    }

    @Override
    public void onClick( View v )
    {
        Intent intent = new Intent( Intent.ACTION_VIEW );
        if ( "video".equals( attachment.getObjectType() ) )
        {
            intent.setData( Uri.parse( attachment.getEmbed().getUrl() ) );
        }
        else
        {
            intent.setData( Uri.parse( attachment.getUrl() ) );
        }
        getContext().startActivity( intent );
    }
}
