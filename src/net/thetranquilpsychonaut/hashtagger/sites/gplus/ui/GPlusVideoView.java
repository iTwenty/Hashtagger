package net.thetranquilpsychonaut.hashtagger.sites.gplus.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.api.services.plus.model.Activity;
import com.squareup.picasso.Picasso;
import net.thetranquilpsychonaut.hashtagger.R;

/**
 * Created by itwenty on 5/14/14.
 */
public class GPlusVideoView extends RelativeLayout
{
    private ImageView imgvVideoThumbnail;
    private TextView  tvVideoTitle;
    private TextView tvVideoDescription;

    public GPlusVideoView( Context context )
    {
        this( context, null, 0 );
    }

    public GPlusVideoView( Context context, AttributeSet attrs )
    {
        this( context, attrs, 0 );
    }

    public GPlusVideoView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        inflate( context, R.layout.gplus_video_view, this );
        imgvVideoThumbnail = ( ImageView ) findViewById( R.id.imgv_video_thumbnail );
        tvVideoTitle = ( TextView ) findViewById( R.id.tv_video_title );
        tvVideoDescription = ( TextView ) findViewById( R.id.tv_video_description );
    }

    public void showVideoThumbnail( Activity activity )
    {
        Picasso.with( getContext() )
                .load( activity.getObject().getAttachments().get( 0 ).getImage().getUrl() )
                .error( R.drawable.drawable_image_loading )
                .fit()
                .centerCrop()
                .into( imgvVideoThumbnail );
        tvVideoTitle.setText( activity.getObject().getAttachments().get( 0 ).getDisplayName() );
        tvVideoDescription.setText( activity.getObject().getAttachments().get( 0 ).getContent() );
    }
}
