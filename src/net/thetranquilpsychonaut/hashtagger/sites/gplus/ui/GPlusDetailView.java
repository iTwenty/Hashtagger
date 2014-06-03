package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusDetailView extends RelativeLayout implements Callback, View.OnClickListener
{
    private ImageView                       imgvThumbnail;
    private TextView                        tvTitle;
    private TextView                        tvDescription;
    private LayerDrawable                   videoDrawable;
    private Activity.PlusObject.Attachments attachment;

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
        imgvThumbnail = ( ImageView ) findViewById( R.id.imgv_thumbnail );
        tvTitle = ( TextView ) findViewById( R.id.tv_title );
        tvDescription = ( TextView ) findViewById( R.id.tv_description );
        videoDrawable = ( LayerDrawable ) getContext().getResources().getDrawable( R.drawable.video );
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
                    .into( imgvThumbnail, this );
        }
        else
        {
            Picasso.with( getContext() )
                    .load( R.drawable.gplus_sketch )
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into( imgvThumbnail, this );
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

    private void overlayPlayButton()
    {
        videoDrawable.setDrawableByLayerId( R.id.image, imgvThumbnail.getDrawable() );
        imgvThumbnail.setImageDrawable( videoDrawable );
    }

    @Override
    public void onSuccess()
    {
        if ( "video".equals( attachment.getObjectType() ) )
        {
            overlayPlayButton();
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