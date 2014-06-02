package net.thetranquilpsychonaut.hashtagger.sites.facebook.ui;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import facebook4j.Post;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/17/14.
 */
public class FacebookDetailView extends RelativeLayout implements Callback
{

    private ImageView     imgvThumbnail;
    private TextView      tvTitle;
    private TextView      tvDescription;
    private LayerDrawable videoDrawable;
    private Post          post;

    public FacebookDetailView( Context context )
    {
        this( context, null, R.attr.GPlusDetailViewStyle );
    }

    public FacebookDetailView( Context context, AttributeSet attrs )
    {
        this( context, attrs, R.attr.GPlusDetailViewStyle );
    }

    public FacebookDetailView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.facebook_detail_view, this );
        imgvThumbnail = ( ImageView ) findViewById( R.id.imgv_thumbnail );
        tvTitle = ( TextView ) findViewById( R.id.tv_title );
        tvDescription = ( TextView ) findViewById( R.id.tv_description );
        videoDrawable = ( LayerDrawable ) getContext().getResources().getDrawable( R.drawable.video );
    }

    public void showDetails( Post post )
    {
        this.post = post;
        if ( null != post.getPicture() )
        {
            Picasso.with( getContext() )
                    .load( post.getPicture().toString() )
                    .error( R.drawable.drawable_image_loading )
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into( imgvThumbnail, this );
        }
        else
        {
            Picasso.with( getContext() )
                    .load( R.drawable.facebook_icon_flat_large )
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into( imgvThumbnail, this );
        }
        tvTitle.setText( post.getName() );
        tvDescription.setText( post.getDescription() );
    }

    private void overlayPlayButton()
    {
        videoDrawable.setDrawableByLayerId( R.id.image, imgvThumbnail.getDrawable() );
        imgvThumbnail.setImageDrawable( videoDrawable );
    }

    @Override
    public void onSuccess()
    {
        if ( "video".equals( post.getType() ) )
        {
            overlayPlayButton();
        }
    }

    @Override
    public void onError()
    {

    }
}
